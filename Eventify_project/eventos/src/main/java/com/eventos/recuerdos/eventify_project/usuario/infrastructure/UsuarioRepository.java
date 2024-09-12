package com.eventos.recuerdos.eventify_project.usuario.infrastructure;

import com.eventos.recuerdos.eventify_project.usuario.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Si quieres buscar un usuario por email
    Optional<Usuario> findByEmail(String email);
}
