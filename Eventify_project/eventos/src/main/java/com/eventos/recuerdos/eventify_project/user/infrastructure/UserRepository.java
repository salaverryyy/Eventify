package com.eventos.recuerdos.eventify_project.user.infrastructure;

import com.eventos.recuerdos.eventify_project.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
