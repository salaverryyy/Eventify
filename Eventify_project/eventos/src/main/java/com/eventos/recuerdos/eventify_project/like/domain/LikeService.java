package com.eventos.recuerdos.eventify_project.like.domain;

import com.eventos.recuerdos.eventify_project.like.infrastructure.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
}
