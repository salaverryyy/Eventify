package com.eventos.recuerdos.eventify_project.usuario.domain;

import com.eventos.recuerdos.eventify_project.usuario.infrastructure.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    // Método para eliminar un usuario
    public void deleteUser(Long id) {
        Optional<Usuario> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete); // Si el usuario existe, lo eliminamos
    }

    // Otros métodos de negocio (por ejemplo, obtener, crear, actualizar)
}
