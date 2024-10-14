package com.eventos.recuerdos.eventify_project.user.infrastructure;

import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);

    UserAccount findByEmail(String email);

    List<UserAccount> findByUsernameContainingIgnoreCase(String username);
}
