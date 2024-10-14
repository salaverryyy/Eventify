package com.eventos.recuerdos.eventify_project.comment.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentDtoValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = new CommentDtoValidator(); // Usar el validador correcto
    }

    @Test
    public void whenDtoIsValid_thenNoErrors() {
        CommentDTO dto = new CommentDTO();
        dto.setContent("Valid comment");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dto, "commentDTO");
        validator.validate(dto, errors);

        assertEquals(0, errors.getErrorCount());
    }

    @Test
    public void whenDtoIsInvalid_thenHasErrors() {
        CommentDTO dto = new CommentDTO();
        dto.setContent(""); // Invalid

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dto, "commentDTO");
        validator.validate(dto, errors);

        assertEquals(1, errors.getErrorCount());
    }
}
