package com.solvd.webappsimple.web.controller;

import com.solvd.webappsimple.domain.Event;
import com.solvd.webappsimple.domain.EventSearchCriteria;
import com.solvd.webappsimple.service.EventService;
import com.solvd.webappsimple.web.annotation.GetMapping;
import com.solvd.webappsimple.web.annotation.PathVariable;
import com.solvd.webappsimple.web.annotation.PostMapping;
import com.solvd.webappsimple.web.annotation.RequestBody;
import com.solvd.webappsimple.web.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/events")
public class EventController extends BaseController {

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        EventService eventService = getService(EventService.class);
        return eventService.createEvent(event);
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") Long id) {
        EventService eventService = getService(EventService.class);
        return eventService.getEventById(id);
    }

    @GetMapping
    public List<Event> getAllUserEvents(HttpServletRequest request) {
        EventService eventService = getService(EventService.class);
        return eventService.getEventsByOwnerId((Long) request.getAttribute("principalId"));
    }

    @GetMapping("/search")
    public List<Event> getEventsBefore(@RequestBody EventSearchCriteria sc) {
        EventService eventService = getService(EventService.class);
        return eventService.getEventsBefore(sc.getBeforeDate());
    }

}
