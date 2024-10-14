package com.eventos.recuerdos.eventify_project.comment.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eventos.recuerdos.eventify_project.comment.application.CommentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(CommentController.class)
public class CommentControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    public void whenUserHasRole_thenAccessGranted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUserIsNotAuthenticated_thenAccessDenied() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comments"))
                .andExpect(status().isUnauthorized());
    }
}
