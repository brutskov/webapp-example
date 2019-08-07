package com.solvd.webappsimple.persistence.impl;

import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.persistence.UserRepository;
import com.solvd.webappsimple.persistence.config.QueryExecutor;
import com.solvd.webappsimple.persistence.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private static final String CREATE_QUERY = "Insert into users(username, password, first_name, last_name) values (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "Select * from users where id = ?";
    private static final String FIND_BY_USERNAME_QUERY = "Select * from users where username = ?";
    private static final String FIND_ALL_QUERY = "Select * from users";
    private static final String UPDATE_SESSION_QUERY = "Update users set session_id = ?, session_expired_in = ? where id = ?";

    @Override
    public User create(User user) {
        QueryExecutor<User> queryExecutor = new QueryExecutor<>(new UserMapper());
        return queryExecutor.save(CREATE_QUERY, user,
                new AbstractMap.SimpleEntry<>("username", user.getUsername()),
                new AbstractMap.SimpleEntry<>("password", user.getPassword()),
                new AbstractMap.SimpleEntry<>("firstName", user.getFirstName()),
                new AbstractMap.SimpleEntry<>("lastName", user.getLastName())
        );
    }

    @Override
    public User findById(Long id) {
        QueryExecutor<User> queryExecutor = new QueryExecutor<>(new UserMapper());
        return queryExecutor.selectOne(FIND_BY_ID_QUERY, new AbstractMap.SimpleEntry<>("id", id));
    }

    @Override
    public User findByUsername(String username) {
        QueryExecutor<User> queryExecutor = new QueryExecutor<>(new UserMapper());
        return queryExecutor.selectOne(FIND_BY_USERNAME_QUERY, new AbstractMap.SimpleEntry<>("username", username));
    }

    @Override
    public List<User> findAll() {
        QueryExecutor<User> queryExecutor = new QueryExecutor<>(new UserMapper());
        return queryExecutor.selectAll(FIND_ALL_QUERY);
    }

    @Override
    public String updateSessionId(String sessionId, LocalDateTime expiredIn, Long userId) {
        QueryExecutor<User> queryExecutor = new QueryExecutor<>(new UserMapper());
        queryExecutor.save(UPDATE_SESSION_QUERY, null,
            new AbstractMap.SimpleEntry<>("sessionId", sessionId),
            new AbstractMap.SimpleEntry<>("sessionExpiredIn", expiredIn),
            new AbstractMap.SimpleEntry<>("id", userId)
        );
        return sessionId;
    }

}
