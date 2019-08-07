package com.solvd.webappsimple.service.impl;

import com.solvd.webappsimple.domain.Event;
import com.solvd.webappsimple.persistence.EventRepository;
import com.solvd.webappsimple.persistence.impl.EventRepositoryImpl;
import com.solvd.webappsimple.service.EventService;
import com.solvd.webappsimple.service.util.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public class EventServiceImpl implements EventService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Event createEvent(Event event) {
        EventRepository eventRepository = new EventRepositoryImpl();
        return eventRepository.createEvent(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long id) {
        EventRepository eventRepository = new EventRepositoryImpl();
        return eventRepository.findEventById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByOwnerId(Long ownerId) {
        EventRepository eventRepository = new EventRepositoryImpl();
        return eventRepository.findEventsByOwnerId(ownerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsBefore(LocalDateTime localDateTime) {
        EventRepository eventRepository = new EventRepositoryImpl();
        return eventRepository.findEventsBefore(localDateTime);
    }

}
