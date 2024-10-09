package com.eventos.recuerdos.eventify_project.invitation.infrastructure;


import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    // Busca la invitación por el código QR
    Optional<Invitation> findByQrCode(String qrCode);

    // Busca la invitación por el enlace de invitación (token)
    Optional<Invitation> findByInvitationLink(String invitationLink);
}
