package com.eventos.recuerdos.eventify_project.memory.infrastructure;


import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {

}
