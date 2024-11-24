package com.eventos.recuerdos.eventify_project.memory.infrastructure;


import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {


    boolean existsByMemoryName(@NotBlank(message = "El título no puede estar en blanco.") @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres.") String memoryName);

    @Modifying
    @Query("DELETE FROM Memory m WHERE m.userAccount.id = :userAccountId")
    void deleteByUserAccountId(@Param("userAccountId") Long userAccountId);


    Memory findByAccessCode(String accessCode);

    List<Memory> findByUserAccountId(Long userId);

    @Query("SELECT m FROM Memory m JOIN m.participants p WHERE p.id = :userId")
    List<Memory> findMemoriesByParticipantsId(@Param("userId") Long userId);

    Memory findByAlbumLinkContaining(String uuid);

}
