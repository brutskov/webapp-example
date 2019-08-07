package com.solvd.webappsimple.service.impl;

import com.solvd.webappsimple.domain.Event;
import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.persistence.EventRepository;
import com.solvd.webappsimple.service.EventService;
import com.solvd.webappsimple.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    public EventServiceImpl(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Event createEvent(Event event) {
        event.setId(null);
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                              .orElseThrow(() -> new RuntimeException("Event with id " + id + " does not exist"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByOwnerUsername(String ownerUsername) {
        User user = userService.getByUsername(ownerUsername);
        return eventRepository.findAllByOwnerId(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsBefore(LocalDateTime localDateTime) {
        return eventRepository.findAllByDateBefore(localDateTime);
    }

}
