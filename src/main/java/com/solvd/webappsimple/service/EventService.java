package com.solvd.webappsimple.service;


import com.solvd.webappsimple.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Event createEvent(Event event);

    Event getEventById(Long id);

    List<Event> getEventsByOwnerUsername(String ownerUsername);

    List<Event> getEventsBefore(LocalDateTime localDateTime);

}
