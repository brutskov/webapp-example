package com.solvd.webappsimple.domain;

import java.time.LocalDateTime;

public class EventSearchCriteria {

    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime beforeDate;

    public LocalDateTime getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(LocalDateTime beforeDate) {
        this.beforeDate = beforeDate;
    }

}
