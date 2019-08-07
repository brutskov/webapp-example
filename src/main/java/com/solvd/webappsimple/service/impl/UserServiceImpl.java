package com.solvd.webappsimple.service.impl;

import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.persistence.UserRepository;
import com.solvd.webappsimple.persistence.impl.UserRepositoryImpl;
import com.solvd.webappsimple.service.UserService;
import com.solvd.webappsimple.service.util.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User create(User user) {
        UserRepository userRepository = new UserRepositoryImpl();
        return userRepository.create(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        UserRepository userRepository = new UserRepositoryImpl();
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        UserRepository userRepository = new UserRepositoryImpl();
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        UserRepository userRepository = new UserRepositoryImpl();
        return userRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateSessionId(String sessionId, LocalDateTime expiredIn, Long userId) {
        UserRepository userRepository = new UserRepositoryImpl();
        return userRepository.updateSessionId(sessionId, expiredIn, userId);
    }

}
