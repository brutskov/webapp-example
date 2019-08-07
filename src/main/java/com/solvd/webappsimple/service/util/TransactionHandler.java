package com.solvd.webappsimple.service.util;

import com.solvd.webappsimple.domain.config.ProxyHandler;
import com.solvd.webappsimple.persistence.config.DataSource;
import com.solvd.webappsimple.persistence.config.TransactionHolder;
import com.solvd.webappsimple.service.Service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class TransactionHandler implements ProxyHandler<Service> {

    private Service service;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Method realMethod = getRealMethod(service, method);
        if (realMethod.isAnnotationPresent(Transactional.class)) {
            Transactional transactional = realMethod.getAnnotation(Transactional.class);
            boolean readOnly = transactional.readOnly();
            Class<? extends Throwable>[] rollbackFor = transactional.rollbackFor();

            result = invokeTransactionalMethod(readOnly, rollbackFor, method, args);

        } else {
            try {
                result = method.invoke(service, args);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Object invokeTransactionalMethod(boolean readOnly, Class<? extends Throwable>[] rollbackFor, Method method, Object[] args) {

        Object result = null;
        Connection connection = null;

        try {
            connection = DataSource.getConnection();
            connection.setReadOnly(readOnly);

            connection.setAutoCommit(false); // <- transaction starting

            TransactionHolder.TransactionHelper transactionHelper = new TransactionHolder.TransactionHelper(connection);
            TransactionHolder.setTransactionHelper(transactionHelper);

            result = method.invoke(service, args);

            connection.commit(); // <- transaction finishing

        } catch (Throwable e) {
            try {
                if (connection != null) {
                    boolean needRollback = Arrays.stream(rollbackFor).anyMatch(exClass -> exClass.isAssignableFrom(e.getCause().getClass()));
                    if(needRollback || SQLException.class.isAssignableFrom(e.getClass())) {
                        connection.rollback(); // rollback
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Method getRealMethod(Object proxy, Method interfaceMethod) {
        Method result = null;
        try {
            result = proxy.getClass().getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void setObject(Service service) {
        this.service = service;
    }

}
