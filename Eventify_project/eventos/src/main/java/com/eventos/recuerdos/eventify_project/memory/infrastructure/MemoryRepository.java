package com.eventos.recuerdos.eventify_project.memory.infrastructure;


import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {


    boolean existsByMemoryName(@NotBlank(message = "El título no puede estar en blanco.") @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres.") String memoryName);

    void deleteByUserAccountId(Long userId);

    Memory findByAccessCode(String accessCode);
}
