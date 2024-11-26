package com.eventos.recuerdos.eventify_project.user.infrastructure;

import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String username); // Método existente que devuelve UserAccount
    Optional<UserAccount> findOptionalByUsername(String username); // Nuevo método que devuelve Optional<UserAccount>
    UserAccount findByEmail(String email);
    List<UserAccount> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
