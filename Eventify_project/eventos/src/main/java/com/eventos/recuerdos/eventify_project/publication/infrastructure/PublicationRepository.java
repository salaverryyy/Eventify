package com.eventos.recuerdos.eventify_project.publication.infrastructure;


import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.dto.PublicationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {
    Collection<Object> findByMemoryId(Long id);
    // Método para encontrar publicaciones por el ID del usuario
    List<Publication> findByAuthor_Id(Long authorId);// Ajustado al campo 'author'


}
