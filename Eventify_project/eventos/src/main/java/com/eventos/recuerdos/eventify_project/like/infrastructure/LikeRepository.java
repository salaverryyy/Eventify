package com.eventos.recuerdos.eventify_project.like.infrastructure;

import com.eventos.recuerdos.eventify_project.like.domain.PublicationLike;
import com.eventos.recuerdos.eventify_project.publication.domain.Publication;
import com.eventos.recuerdos.eventify_project.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<PublicationLike, Long> {

    List<PublicationLike> findByPublication(Publication publication);

    Optional<PublicationLike> findByPublicationAndUser(Publication publication, User user);
}
