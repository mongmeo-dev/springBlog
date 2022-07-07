package dev.mongmeo.springblog.service;

import dev.mongmeo.springblog.dto.PostRequestDto;
import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostService {

  private final PostRepository postRepository;

  public List<PostResponseDto> getAllPosts() {
    List<PostEntity> posts = postRepository.findAll();

    return posts.stream().map(PostEntity::toResponseDto).collect(Collectors.toList());
  }

  public PostResponseDto createPost(PostRequestDto dto) {
    PostEntity savedPost = postRepository.save(PostEntity.fromRequestDto(dto));
    
    return PostEntity.toResponseDto(savedPost);
  }
}
