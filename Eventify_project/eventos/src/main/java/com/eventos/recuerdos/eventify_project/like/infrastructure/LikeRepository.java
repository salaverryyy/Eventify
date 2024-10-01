package com.eventos.recuerdos.eventify_project.like.infrastructure;

import com.eventos.recuerdos.eventify_project.like.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
