package com.solvd.webappsimple.persistence;

import com.solvd.webappsimple.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository {

    User create(User user);

    User findById(Long id);

    User findByUsername(String username);

    List<User> findAll();

    String updateSessionId(String sessionId, LocalDateTime expiredIn, Long userId);

}
