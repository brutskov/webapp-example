package com.solvd.webappsimple.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String sessionId;

    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime sessionExpiredIn;

    public User() {
    }

    public User(Long id, String username, String password, String firstName, String lastName) {
        setId(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getSessionExpiredIn() {
        return sessionExpiredIn;
    }

    public void setSessionExpiredIn(LocalDateTime sessionExpiredIn) {
        this.sessionExpiredIn = sessionExpiredIn;
    }

}
