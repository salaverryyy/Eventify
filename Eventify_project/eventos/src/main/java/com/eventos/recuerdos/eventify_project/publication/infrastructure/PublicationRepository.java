package com.eventos.recuerdos.eventify_project.publication.infrastructure;


import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    Collection<Object> findByMemoryId(Long id);
}
