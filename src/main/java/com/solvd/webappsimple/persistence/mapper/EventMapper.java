package com.solvd.webappsimple.persistence.mapper;

import com.solvd.webappsimple.domain.Event;
import com.solvd.webappsimple.domain.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class EventMapper implements ResultMapper<Event> {

    @Override
    public Event mapResult(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        Event.Type type = Event.Type.valueOf(resultSet.getString("type"));
        String schedule = resultSet.getString(resultSet.getString("schedule"));
        Date date = resultSet.getDate("date");

        User user = null;
        if (resultSet.getString("user_username") != null) {
            UserMapper userMapper = new UserMapper();
            userMapper.setPrefix("user_");
            userMapper.mapResult(resultSet);
        }

        Event event = new Event();
        event.setId(id);
        event.setType(type);
        event.setSchedule(schedule);
        event.setOwner(user);

        if (date != null) {
            LocalDateTime eventDate = date.toLocalDate().atTime(0, 0);
            event.setDate(eventDate);
        }
        return event;
    }

    @Override
    public Class<Event> getTargetClass() {
        return Event.class;
    }

}
