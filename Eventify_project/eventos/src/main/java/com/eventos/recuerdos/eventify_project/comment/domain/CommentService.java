package com.eventos.recuerdos.eventify_project.comment.domain;

import com.eventos.recuerdos.eventify_project.comment.infrastructure.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
}
