package com.solvd.webappsimple.web;

import com.solvd.webappsimple.domain.Event;
import com.solvd.webappsimple.domain.EventSearchCriteria;
import com.solvd.webappsimple.service.EventService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping
    public List<Event> getAllUserEvents() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return eventService.getEventsByOwnerUsername(user.getUsername());
    }

    @GetMapping("/search")
    public List<Event> getEventsBefore(@RequestBody EventSearchCriteria sc) {
        return eventService.getEventsBefore(sc.getBeforeDate());
    }

}
