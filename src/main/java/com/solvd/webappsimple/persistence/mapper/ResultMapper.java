package com.solvd.webappsimple.persistence.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultMapper<T> {

    T mapResult(ResultSet resultSet) throws SQLException;

    Class<T> getTargetClass();

}
