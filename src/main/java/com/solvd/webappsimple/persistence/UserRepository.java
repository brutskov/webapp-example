package com.solvd.webappsimple.persistence;

import com.solvd.webappsimple.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("Update User u set u.sessionId = :sessionId, u.sessionExpiredIn = :expiredIn where u.id = :userId")
    User updateSessionId(String sessionId, LocalDateTime expiredIn, Long userId);

}
