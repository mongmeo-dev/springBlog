package dev.mongmeo.springblog.repository;

import dev.mongmeo.springblog.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
