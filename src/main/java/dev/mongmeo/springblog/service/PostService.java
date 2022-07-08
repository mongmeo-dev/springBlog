package dev.mongmeo.springblog.service;

import dev.mongmeo.springblog.dto.PostCreateDto;
import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.dto.PostUpdateDto;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.exception.NotFoundException;
import dev.mongmeo.springblog.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;

  public List<PostResponseDto> getAllPosts(String page, String size) {
    List<PostEntity> posts;

    if (!page.isBlank() && !size.isBlank()) {
      PageRequest pageRequest = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(size));
      posts = postRepository.findAll(pageRequest).getContent();
    } else {
      posts = postRepository.findAll();
    }

    return posts.stream().map(PostEntity::toResponseDto).collect(Collectors.toList());
  }

  public PostResponseDto getPostById(long id) {
    PostEntity foundPost = postRepository.findById(id).orElseThrow(NotFoundException::new);

    return PostEntity.toResponseDto(foundPost);
  }

  public PostResponseDto createPost(PostCreateDto dto) {
    PostEntity savedPost = postRepository.save(PostEntity.fromRequestDto(dto));

    return PostEntity.toResponseDto(savedPost);
  }

  public PostResponseDto updatePost(long id, PostUpdateDto dto) {
    PostEntity foundPost = postRepository.findById(id).orElseThrow(NotFoundException::new);

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
