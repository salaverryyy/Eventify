package com.eventos.recuerdos.eventify_project.evento.application;

import com.eventos.recuerdos.eventify_project.evento.domain.Evento;
import com.eventos.recuerdos.eventify_project.evento.domain.EventoService;
import com.eventos.recuerdos.eventify_project.evento.insfrastructure.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private EventoService eventoService;

    @GetMapping
    public List<Evento> getAllEventos() {
        return eventoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        return evento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Evento createEvento(@RequestBody Evento evento) {
        return eventoRepository.save(evento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> updateEvento(@PathVariable Long id, @RequestBody Evento eventoDetails) {
        return eventoRepository.findById(id).map(evento -> {
            evento.setNombre(eventoDetails.getNombre());
            evento.setFecha(eventoDetails.getFecha());
            evento.setUbicacion(eventoDetails.getUbicacion());
            evento.setQr(eventoDetails.getQr());
            evento.setPrivacidad(eventoDetails.getPrivacidad());
            return ResponseEntity.ok(eventoRepository.save(evento));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        eventoService.deleteEvento(id);
        return ResponseEntity.noContent().build();
    }
}
