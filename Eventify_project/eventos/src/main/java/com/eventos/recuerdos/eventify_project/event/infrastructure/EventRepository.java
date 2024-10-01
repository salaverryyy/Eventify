package com.eventos.recuerdos.eventify_project.event.infrastructure;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
