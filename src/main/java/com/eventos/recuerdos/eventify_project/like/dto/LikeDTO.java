package com.eventos.recuerdos.eventify_project.like.dto;


import lombok.Data;

@Data
public class LikeDTO {
    private Long id;
    private Long userId;
    private Long publicationId;
}
//Transferir información relacionada con los "me gusta" en las publicaciones