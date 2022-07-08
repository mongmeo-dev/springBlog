package dev.mongmeo.springblog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.mongmeo.springblog.dto.PostCreateDto;
import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.dto.PostUpdateDto;
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
    createDummyPostsAndGetLastPostId();

    // when
    List<PostResponseDto> allPosts = postService.getAllPosts("", "");

    //then
    assertEquals(10, allPosts.size());
  }

  @Test
  @DisplayName("페이지 정보와 함께 호출시 해당 페이지의 게시물 리스트를 반환해야 함")
  void getAllPostWithPage() {
    // given
    createDummyPostsAndGetLastPostId();

    // when
    List<PostResponseDto> pagePosts = postService.getAllPosts("1", "3");

    // then
    assertEquals(3, pagePosts.size());
  }

  @Test
  @DisplayName("특정 id를 가진 게시물을 반환해야 함")
  void getPostByIdTest() {
    // given
    long id = createDummyPostsAndGetLastPostId();

    // when
    PostResponseDto foundPost = postService.getPostById(id);

    //then
    assertEquals(foundPost.getId(), id);
  }

  @Test
  @DisplayName("특정 id를 가진 게시물이 없다면 NotFoundException을 발생시켜야 함")
  void getPostByIdExceptionTest() {
    // given
    createDummyPostsAndGetLastPostId();

    // when, then
    assertThrows(NotFoundException.class, () -> {
      postService.getPostById(100);
    });
  }

  @Test
  @DisplayName("새 포스트가 등록되어야 함")
  void createPostTest() {
    // given
    PostCreateDto dto = PostCreateDto.builder()
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

  @Test
  @DisplayName("요청에 따라 업데이트 된 게시물을 반환해야 함")
  void updatePostTest() {
    // given
    long id = createDummyPostsAndGetLastPostId();

    // when
    PostUpdateDto dto = PostUpdateDto.builder().title("hello").content("world").build();
    PostResponseDto updatedPost = postService.updatePost(id, dto);

    //then
    assertEquals(updatedPost.getId(), id);
    assertEquals(updatedPost.getTitle(), dto.getTitle());
    assertEquals(updatedPost.getContent(), dto.getContent());
  }

  @Test
  @DisplayName("업데이트 요청시 특정 id를 가진 게시물이 없다면 NotFoundException을 발생시켜야 함")
  void updatePostExceptionTest() {
    // given
    createDummyPostsAndGetLastPostId();

    // when, then
    assertThrows(NotFoundException.class, () -> {
      postService.updatePost(10000, new PostUpdateDto());
    });
  }

  @Test
  @DisplayName("전달받은 id를 가진 게시물을 삭제해야 함")
  void deletePostTest() {
    // given
    long id = createDummyPostsAndGetLastPostId();

    // when
    postService.deletePost(id);

    // then
    assertEquals(9, postRepository.count());
    assertFalse(postRepository.findById(id).isPresent());
  }

  private long createDummyPostsAndGetLastPostId() {
    PostEntity entity = new PostEntity();
    for (int i = 0; i < 10; i++) {
      PostEntity newPost = PostEntity.builder()
          .title("title" + i)
          .content("content" + i)
          .build();

      entity = postRepository.save(newPost);
    }

    return entity.getId();
  }
}