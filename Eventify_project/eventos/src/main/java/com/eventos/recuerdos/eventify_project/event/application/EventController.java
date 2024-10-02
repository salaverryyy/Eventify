package com.eventos.recuerdos.eventify_project.event.application;

import com.eventos.recuerdos.eventify_project.event.domain.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventService eventService;
}
