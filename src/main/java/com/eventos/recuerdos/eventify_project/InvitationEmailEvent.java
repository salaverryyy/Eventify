package com.eventos.recuerdos.eventify_project;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class InvitationEmailEvent extends ApplicationEvent {
    private final String email;
    private final String qrCode; // QR en base64
    private final String albumLink;

    public InvitationEmailEvent(String email, String qrCode, String albumLink) {
        super(email);
        this.email = email;
        this.qrCode = qrCode;
        this.albumLink = albumLink;
    }
}

