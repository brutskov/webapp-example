package com.solvd.webappsimple.persistence;

import com.solvd.webappsimple.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository {

    Event createEvent(Event event);

    Event findEventById(Long id);

    List<Event> findEventsByOwnerId(Long ownerId);

    List<Event> findEventsBefore(LocalDateTime localDateTime);

}
