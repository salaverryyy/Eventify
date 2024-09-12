package com.eventos.recuerdos.eventify_project.evento.domain;

import com.eventos.recuerdos.eventify_project.evento.insfrastructure.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    public void deleteEvento(Long id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        evento.ifPresent(eventoRepository::delete);
    }

}
