package com.eventos.recuerdos.eventify_project.publication.application;

import com.eventos.recuerdos.eventify_project.publication.domain.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publication")
public class PublicationController {
    @Autowired
    private PublicationService publicationService;
}
