package com.eventos.recuerdos.eventify_project.event.infrastructure;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    void deleteByOrganizer_Id(Long organizerId);
    Event findEventById(Long id);


}
