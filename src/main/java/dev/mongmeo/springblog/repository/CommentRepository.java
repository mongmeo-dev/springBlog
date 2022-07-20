package dev.mongmeo.springblog.repository;

import dev.mongmeo.springblog.entity.CommentEntity;
import dev.mongmeo.springblog.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

  long countByPost(PostEntity post);
}
