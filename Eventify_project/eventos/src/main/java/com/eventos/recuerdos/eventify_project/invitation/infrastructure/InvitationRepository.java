package com.eventos.recuerdos.eventify_project.invitation.infrastructure;


import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

}
