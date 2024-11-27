package com.eventos.recuerdos.eventify_project;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class HelloEmailEvent extends ApplicationEvent {
    private final String email;

    public HelloEmailEvent(String email) {
        super(email);
        this.email = email;
    }

}