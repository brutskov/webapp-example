package com.solvd.webappsimple.persistence.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

public class TransactionHolder {

    private static final ThreadLocal<TransactionHelper> TRANSACTION_HELPER_THREAD_LOCAL = new ThreadLocal<>();

    public static TransactionHelper getTransactionHelper() {
        return TRANSACTION_HELPER_THREAD_LOCAL.get();
    }

    public static void setTransactionHelper(TransactionHelper transactionHelper) {
        TRANSACTION_HELPER_THREAD_LOCAL.set(transactionHelper);
    }

    public static class TransactionHelper {

        private Connection connection;
        private String query;

        public TransactionHelper(Connection connection) {
            this.connection = connection;
        }

        public <R> R doTransaction(Function<PreparedStatement, R> transactionAction) {
            R result = null;
            if (connection != null && query != null) {
                PreparedStatement preparedStatement = null;

                try {

                    preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    result = transactionAction.apply(preparedStatement);

                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }

}
