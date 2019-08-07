package com.solvd.webappsimple.service;

import com.solvd.webappsimple.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends Service {

    Event createEvent(Event event);

    Event getEventById(Long id);

    List<Event> getEventsByOwnerId(Long ownerId);

    List<Event> getEventsBefore(LocalDateTime localDateTime);

}
