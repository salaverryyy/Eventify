package com.eventos.recuerdos.eventify_project.invitation.infrastructure;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationStatus;
import com.eventos.recuerdos.eventify_project.memory.domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByQrCode(String qrCode);

    @Query("SELECT i FROM Invitation i WHERE i.memory.id = :memoryId AND i.status = :status")
    List<Invitation> findAllByMemoryIdAndStatus(@Param("memoryId") Long memoryId, @Param("status") InvitationStatus status);

    @Query("SELECT i.memory FROM Invitation i WHERE i.usuarioInvitado.id = :userId AND i.status = 'ACCEPTED'")
    List<Memory> findAcceptedMemoriesByUserId(@Param("userId") Long userId);

    @Query("SELECT i FROM Invitation i WHERE i.usuarioInvitado.id = :userId AND i.status = :status")
    List<Invitation> findAllByUserIdAndStatus(@Param("userId") Long userId, @Param("status") InvitationStatus status);

    @Query("SELECT i FROM Invitation i WHERE i.usuarioInvitado.username = :username AND i.status = :status")
    List<Invitation> findAllByUsernameAndStatus(@Param("username") String username, @Param("status") InvitationStatus status);

    @Query("SELECT i FROM Invitation i WHERE i.usuarioInvitado.email = :email AND i.status = :status")
    List<Invitation> findAllByEmailAndStatus(@Param("email") String email, @Param("status") InvitationStatus status);

    Optional<Invitation> findById(Long id);

    @Query("SELECT i FROM Invitation i WHERE i.uuid = :uuid")
    Optional<Invitation> findByUuid(@Param("uuid") String uuid);

}
