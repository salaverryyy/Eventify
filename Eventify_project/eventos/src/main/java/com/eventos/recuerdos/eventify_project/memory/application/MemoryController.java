package com.eventos.recuerdos.eventify_project.memory.application;

import com.eventos.recuerdos.eventify_project.memory.domain.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory")
public class MemoryController {
    @Autowired
    private MemoryService memoryService;
}
