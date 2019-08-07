package com.solvd.webappsimple.persistence.impl;

import com.solvd.webappsimple.domain.Event;
import com.solvd.webappsimple.persistence.EventRepository;
import com.solvd.webappsimple.persistence.config.QueryExecutor;
import com.solvd.webappsimple.persistence.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;

public class EventRepositoryImpl implements EventRepository {

    private static final String CREATE_QUERY = "Insert into events(owner_id, date, type, schedule) values (?, ?, ?, ?)";
    private static final String FIND_QUERY = "Select u.id as user_id, u.username as user_username, u.first_name as user_first_name, u.last_name as user_last_name, e.* from events e left join users u on e.owner_id = u.id";
    private static final String FIND_BY_ID_QUERY = FIND_QUERY + " where id = ?";
    private static final String FIND_BY_OWNER_ID_QUERY = FIND_QUERY + " where owner_id = ?";
    private static final String FIND_BEFORE_QUERY = FIND_QUERY + " where date > CURRENT_TIMESTAMP and date < ?";

    @Override
    public Event createEvent(Event event) {
        QueryExecutor<Event> queryExecutor = new QueryExecutor<>(new EventMapper());
        return queryExecutor.save(CREATE_QUERY, event,
                new AbstractMap.SimpleEntry<>("owner.id", event.getOwner().getId()),
                new AbstractMap.SimpleEntry<>("date", event.getDate()),
                new AbstractMap.SimpleEntry<>("type", event.getType().name()),
                new AbstractMap.SimpleEntry<>("schedule", event.getSchedule())
        );
    }

    @Override
    public Event findEventById(Long id) {
        QueryExecutor<Event> queryExecutor = new QueryExecutor<>(new EventMapper());
        return queryExecutor.selectOne(FIND_BY_ID_QUERY, new AbstractMap.SimpleEntry<>("id", id));
    }

    @Override
    public List<Event> findEventsByOwnerId(Long ownerId) {
        QueryExecutor<Event> queryExecutor = new QueryExecutor<>(new EventMapper());
        return queryExecutor.selectAll(FIND_BY_OWNER_ID_QUERY, new AbstractMap.SimpleEntry<>("owner.id", ownerId));
    }

    @Override
    public List<Event> findEventsBefore(LocalDateTime date) {
        QueryExecutor<Event> queryExecutor = new QueryExecutor<>(new EventMapper());
        return queryExecutor.selectAll(FIND_BEFORE_QUERY, new AbstractMap.SimpleEntry<>("date", date));
    }

}
