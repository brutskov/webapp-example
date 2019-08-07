package com.solvd.webappsimple.service.impl;

import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.persistence.UserRepository;
import com.solvd.webappsimple.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User create(User user) {
        user.setId(null);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new RuntimeException("User with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new RuntimeException("User with username " + username + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().spliterator().forEachRemaining(users::add);
        return users;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateSessionId(String sessionId, LocalDateTime expiredIn, Long userId) {
        return userRepository.updateSessionId(sessionId, expiredIn, userId)
                             .getSessionId();
    }

}
