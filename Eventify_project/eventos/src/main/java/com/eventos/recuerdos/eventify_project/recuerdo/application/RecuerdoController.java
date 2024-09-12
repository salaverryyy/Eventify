package com.eventos.recuerdos.eventify_project.recuerdo.application;

import com.eventos.recuerdos.eventify_project.recuerdo.domain.Recuerdo;
import com.eventos.recuerdos.eventify_project.recuerdo.domain.RecuerdoService;
import com.eventos.recuerdos.eventify_project.recuerdo.insfrastructure.RecuerdoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recuerdos")
public class RecuerdoController {

    @Autowired
    private RecuerdoRepository recuerdoRepository;
    @Autowired
    private RecuerdoService recuerdoService;

    @GetMapping
    public List<Recuerdo> getAllRecuerdos() {
        return recuerdoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recuerdo> getRecuerdoById(@PathVariable Long id) {
        Optional<Recuerdo> recuerdo = recuerdoRepository.findById(id);
        return recuerdo.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Recuerdo createRecuerdo(@RequestBody Recuerdo recuerdo) {
        return recuerdoRepository.save(recuerdo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recuerdo> updateRecuerdo(@PathVariable Long id, @RequestBody Recuerdo recuerdoDetails) {
        return recuerdoRepository.findById(id).map(recuerdo -> {
            recuerdo.setTipo(recuerdoDetails.getTipo());
            recuerdo.setUrl(recuerdoDetails.getUrl());
            recuerdo.setFechaSubida(recuerdoDetails.getFechaSubida());
            return ResponseEntity.ok(recuerdoRepository.save(recuerdo));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecuerdo(@PathVariable Long id) {
        recuerdoService.deleteRecuerdo(id);
        return ResponseEntity.noContent().build();
    }
}
