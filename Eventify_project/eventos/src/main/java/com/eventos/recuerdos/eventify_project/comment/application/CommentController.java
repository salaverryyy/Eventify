package com.eventos.recuerdos.eventify_project.comment.application;

import com.eventos.recuerdos.eventify_project.comment.domain.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;
}
