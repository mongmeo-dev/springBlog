package dev.mongmeo.springblog.service;

import dev.mongmeo.springblog.dto.post.PostCreateDto;
import dev.mongmeo.springblog.dto.post.PostResponseDto;
import dev.mongmeo.springblog.dto.post.PostUpdateDto;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.exception.NotFoundException;
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
public class PostService {

  private final PostRepository postRepository;
  private final MessageSource messageSource;

  public List<PostResponseDto> getAllPosts(Integer page, Integer size) {
    List<PostEntity> posts;

    if (page != null && size != null) {
      PageRequest pageRequest = PageRequest.of(page - 1, size);
      posts = postRepository.findAll(pageRequest).getContent();
    } else {
      posts = postRepository.findAll();
    }

    return posts.stream().map(PostEntity::toResponseDto).collect(Collectors.toList());
  }

  public long getPostsCount() {
    return postRepository.count();
  }

  public PostResponseDto getPostById(long id) {
    PostEntity foundPost = postRepository.findById(id).orElseThrow(() -> {
      throw new NotFoundException(
          messageSource.getMessage("post_not_found", null, LocaleContextHolder.getLocale())
      );
    });

    return PostEntity.toResponseDto(foundPost);
  }

  public PostResponseDto createPost(PostCreateDto dto) {
    PostEntity savedPost = postRepository.save(PostEntity.fromRequestDto(dto));

    return PostEntity.toResponseDto(savedPost);
  }

  public PostResponseDto updatePost(long id, PostUpdateDto dto) {
    PostEntity foundPost = postRepository.findById(id).orElseThrow(() -> {
      throw new NotFoundException(
          messageSource.getMessage("post_not_found", null, LocaleContextHolder.getLocale())
      );
    });

    if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
      foundPost.setTitle(dto.getTitle());
    }
    if (dto.getContent() != null && !dto.getContent().isBlank()) {
      foundPost.setContent(dto.getContent());
    }

    PostEntity savedPost = postRepository.save(foundPost);

    return PostEntity.toResponseDto(savedPost);
  }

  public void deletePost(long id) {
    postRepository.deleteById(id);
  }
}
