package com.solvd.webappsimple.persistence.mapper;

import com.solvd.webappsimple.domain.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserMapper implements ResultMapper<User> {

    private String prefix = "";

    @Override
    public User mapResult(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(prefix + "id");
        String username = resultSet.getString(prefix + "username");
        String password = resultSet.getString(prefix + "password");
        String firstName = resultSet.getString(prefix + "first_name");
        String lastName = resultSet.getString(prefix + "last_name");
        User user = new User(id, username, password, firstName, lastName);

        String sessionId = resultSet.getString(prefix + "session_id");
        user.setSessionId(sessionId);

        Date expiredIn = resultSet.getDate(prefix + "session_expired_in");
        if (expiredIn != null) {
            LocalDateTime sessionExpiredIn = expiredIn.toLocalDate().atTime(0, 0);
            user.setSessionExpiredIn(sessionExpiredIn);
        }
        return user;
    }

    @Override
    public Class<User> getTargetClass() {
        return User.class;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}
