package com.eventos.recuerdos.eventify_project.event.infrastructure;

import com.eventos.recuerdos.eventify_project.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Modifying
    @Query("DELETE FROM Event e WHERE e.organizer.id = :organizerId")
    void deleteByOrganizer_Id(@Param("organizerId") Long organizerId);

    Event findEventById(Long id);


}
