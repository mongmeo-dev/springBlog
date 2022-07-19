package dev.mongmeo.springblog.service;

import dev.mongmeo.springblog.dto.comment.CommentResponseDto;
import dev.mongmeo.springblog.entity.CommentEntity;
import dev.mongmeo.springblog.exception.NotFoundException;
import dev.mongmeo.springblog.repository.CommentRepository;
import dev.mongmeo.springblog.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final MessageSource messageSource;


  public List<CommentResponseDto> getAllCommentsByPostId(long postId, Integer page, Integer size) {
    validatePostId(postId);

    List<CommentEntity> comments;

    if (page != null && size != null) {
      PageRequest pageRequest = PageRequest.of(page - 1, size);
      comments = commentRepository.findAll(pageRequest).getContent();
    } else {
      comments = commentRepository.findAll();
    }

    return comments.stream().map(CommentEntity::toResponse).collect(Collectors.toList());
  }

  private void validatePostId(long postId) {
    boolean isExist = postRepository.existsById(postId);
    if (!isExist) {
      throw new NotFoundException(
          messageSource.getMessage("post_not_found", null, LocaleContextHolder.getLocale())
      );
    }
  }

}