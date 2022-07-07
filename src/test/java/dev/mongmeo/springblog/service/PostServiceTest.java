package dev.mongmeo.springblog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.mongmeo.springblog.dto.PostRequestDto;
import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.exception.NotFoundException;
import dev.mongmeo.springblog.repository.PostRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostServiceTest {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private PostService postService;

  @Test
  @DisplayName("모든 게시물 리스트를 반환해야 함")
  void getAllPostTest() {
    // given
    createDummyPosts();

    // when
    List<PostResponseDto> allPosts = postService.getAllPosts();

    //then
    assertEquals(10, allPosts.size());
  }

  @Test
  @DisplayName("특정 id를 가진 게시물을 반환해야 함")
  void getPostByIdTest() {
    // given
    createDummyPosts();

    // when
    long id = 2;
    PostResponseDto foundPost = postService.getPostById(id);

    //then
    assertEquals(foundPost.getId(), id);
  }

  @Test
  @DisplayName("특정 id를 가진 게시물이 없다면 NotFoundException을 발생시켜야 함")
  void getPostByIdExceptionTest() {
    // given
    createDummyPosts();

    // when, then
    assertThrows(NotFoundException.class, () -> {
      postService.getPostById(100);
    });
  }

  @Test
  @DisplayName("새 포스트가 등록되어야 함")
  void createPostTest() {
    // given
    PostRequestDto dto = PostRequestDto.builder()
        .title("test title")
        .content("test content")
        .build();

    // when
    PostResponseDto savedPost = postService.createPost(dto);

    // then
    PostEntity foundPost = postRepository.findById(savedPost.getId()).get();
    Assertions.assertThat(foundPost).isNotNull();
    assertEquals(savedPost.getTitle(), foundPost.getTitle());
    assertEquals(savedPost.getContent(), foundPost.getContent());
  }

  private void createDummyPosts() {
    for (int i = 0; i < 10; i++) {
      PostEntity newPost = PostEntity.builder()
          .title("title" + i)
          .content("content" + i)
          .build();

      postRepository.save(newPost);
    }
  }
}