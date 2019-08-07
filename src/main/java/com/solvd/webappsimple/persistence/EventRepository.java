package com.solvd.webappsimple.persistence;

import com.solvd.webappsimple.domain.Event;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findAllByOwnerId(Long ownerId);

    List<Event> findAllByDateBefore(LocalDateTime date);

}
