package com.eventos.recuerdos.eventify_project.like.application;

import com.eventos.recuerdos.eventify_project.like.domain.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;
}
