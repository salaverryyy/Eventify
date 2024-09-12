package com.eventos.recuerdos.eventify_project.recuerdo.domain;


import com.eventos.recuerdos.eventify_project.recuerdo.insfrastructure.RecuerdoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecuerdoService {

    @Autowired
    private RecuerdoRepository recuerdoRepository;

    public void deleteRecuerdo(Long id) {
        Optional<Recuerdo> recuerdo = recuerdoRepository.findById(id);
        recuerdo.ifPresent(recuerdoRepository::delete);
    }
}
