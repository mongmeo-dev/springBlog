package dev.mongmeo.springblog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.repository.PostRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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