package com.eventos.recuerdos.eventify_project.user.application;

import com.eventos.recuerdos.eventify_project.invitation.domain.Invitation;
import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.publication.domain.PublicationService;
import com.eventos.recuerdos.eventify_project.user.domain.AdminService;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import com.eventos.recuerdos.eventify_project.user.domain.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/invitations")
    public ResponseEntity<List<Invitation>> getAllInvitations() {
        List<Invitation> invitations = adminService.getAllInvitations();
        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/publications")
    public ResponseEntity<List<Publication>> getAllPublications() {
        List<Publication> publications = adminService.getAllPublications();
        return ResponseEntity.ok(publications);
    }
}