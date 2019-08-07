package com.solvd.webappsimple.persistence.config;

import com.solvd.webappsimple.domain.BaseEntity;
import com.solvd.webappsimple.persistence.mapper.ResultMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class QueryExecutor<T extends BaseEntity> {

    private final ResultMapper<T> resultMapper;

    public QueryExecutor(ResultMapper<T> resultMapper) {
        this.resultMapper = resultMapper;
    }

    public T selectOne(String query, AbstractMap.SimpleEntry<String, Object>... parameters) {
        return executeQuery(query, resultSet -> {
            T result = null;
            try {
                if (resultSet.next()) {
                    result = resultMapper.mapResult(resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }, parameters);
    }

    public List<T> selectAll(String query, AbstractMap.SimpleEntry<String, Object>... parameters) {
        return executeQuery(query, resultSet -> {
            List<T> results = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    T t = resultMapper.mapResult(resultSet);
                    results.add(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return results;
        }, parameters);
    }

    public T save(String query, T entity, AbstractMap.SimpleEntry<String, Object>... parameters) {
        return executeUpdate(query, entity, parameters);
    }

    private <R extends BaseEntity> R executeUpdate(String query, R entity, AbstractMap.SimpleEntry<String, Object>... parameters) {
        return execute(query, preparedStatement -> {
            try {
                insertParameters(preparedStatement, resultMapper.getTargetClass(), parameters);
                preparedStatement.executeUpdate();
                if (entity != null) {
                    try (ResultSet generatedKeysSet = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeysSet.next()) {
                            entity.setId(generatedKeysSet.getLong("id"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return entity;
        });
    }

    private <R> R executeQuery(String query, Function<ResultSet, R> resultSetAction, AbstractMap.SimpleEntry<String, Object>... parameters) {
        return execute(query, preparedStatement -> {
            R result = null;
            insertParameters(preparedStatement, resultMapper.getTargetClass(), parameters);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                result = resultSetAction.apply(resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        });
    }

    private <R> R execute(String query, Function<PreparedStatement, R> preparedStatementApplier) {
        TransactionHolder.TransactionHelper transactionHelper = TransactionHolder.getTransactionHelper();
        transactionHelper.setQuery(query);
        return transactionHelper.doTransaction(preparedStatementApplier);
    }

    private void insertParameters(PreparedStatement preparedStatement, Class<?> entityClass, AbstractMap.SimpleEntry<String, Object>... parameters) {
        IntStream.range(0, parameters.length).forEach(index -> {
            String fieldName = parameters[index].getKey();
            Object value = parameters[index].getValue();
            Class fieldType = null;
            if (fieldName.contains(".")) {
                String[] slices = fieldName.split("\\.");
                fieldType = entityClass;
                for (String slice : slices) {
                    fieldType = getClassByFieldName(fieldType, slice);
                }
            } else {
                fieldType = getClassByFieldName(entityClass, fieldName);
            }

            if(value != null) {
                try {
                    if (String.class.isAssignableFrom(fieldType)) {
                        preparedStatement.setString(index + 1, value.toString());
                    } else if (Long.class.isAssignableFrom(fieldType)) {
                        preparedStatement.setLong(index + 1, (Long) value);
                    } else if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                        preparedStatement.setDate(index + 1, Date.valueOf(((LocalDateTime) value).toLocalDate()));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Class<?> getClassByFieldName(Class entityClass, String fieldName) {
        Class<?> result = null;
        try {
            result = entityClass.getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            if (entityClass.getSuperclass() != null) {
                result = getClassByFieldName(entityClass.getSuperclass(), fieldName);
            } else {
                e.printStackTrace();
            }
        }
        return result;
    }

}
