package com.solvd.webappsimple.service;

import com.solvd.webappsimple.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService extends Service {

    User create(User user);

    User getById(Long id);

    User getByUsername(String username);

    List<User> getAll();

    String updateSessionId(String sessionId, LocalDateTime expiredIn, Long userId);

}
