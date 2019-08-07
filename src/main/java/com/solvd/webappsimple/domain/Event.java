package com.solvd.webappsimple.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.solvd.webappsimple.web.util.formatter.LocalDateTimeDeserializer;
import com.solvd.webappsimple.web.util.formatter.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class Event extends BaseEntity {

    private Type type;
    private String schedule;
    private User owner;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    public enum Type {
        BIRTHDAY
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
