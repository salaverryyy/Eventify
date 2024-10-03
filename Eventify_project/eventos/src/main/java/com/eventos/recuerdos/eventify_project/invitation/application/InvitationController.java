package com.eventos.recuerdos.eventify_project.invitation.application;

import com.eventos.recuerdos.eventify_project.invitation.domain.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
public class InvitationController {
    @Autowired
    private InvitationService invitationService;
}
