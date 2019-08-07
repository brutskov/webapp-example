package com.solvd.webappsimple.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.solvd.webappsimple.domain.config.formatter.LocalDateTimeDeserializer;
import com.solvd.webappsimple.domain.config.formatter.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class EventSearchCriteria {

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime beforeDate;

    public LocalDateTime getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(LocalDateTime beforeDate) {
        this.beforeDate = beforeDate;
    }

}
