package com.eventos.recuerdos.eventify_project.comment.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDate commentDate;
}

//Transferir los comentarios realizados en publicaciones
