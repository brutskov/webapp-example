package com.solvd.webappsimple.domain;

import java.time.LocalDateTime;

public class EventSearchCriteria {

    private LocalDateTime beforeDate;

    public LocalDateTime getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(LocalDateTime beforeDate) {
        this.beforeDate = beforeDate;
    }

}
