package com.eventos.recuerdos.eventify_project.user.infrastructure;

import com.eventos.recuerdos.eventify_project.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Si quieres buscar un usuario por email
    Optional<User> findByEmail(String email);
}
