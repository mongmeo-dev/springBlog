package dev.mongmeo.springblog.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.service.PostService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PostController.class)
@ExtendWith(MockitoExtension.class)
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @Test
  @DisplayName("모든 게시물 리스트를 json 형식의 postResponseDto 리스트로 받아야 함")
  void getAllPostsTest() throws Exception {
    // given
    Mockito.when(postService.getAllPosts()).thenReturn(createDummyPosts());

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id").exists())
        .andExpect(jsonPath("$[*].title").exists())
        .andExpect(jsonPath("$[*].content").exists())
        .andExpect(jsonPath("$[*].createdAt").exists())
        .andExpect(jsonPath("$[*].updatedAt").exists())
        .andExpect(jsonPath("$.length()").value(5))
        .andDo(print());
  }

  private List<PostResponseDto> createDummyPosts() {
    List<PostResponseDto> posts = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      PostEntity newPost = PostEntity.builder()
          .title("title" + i)
          .content("content" + i)
          .build();

      newPost.setId((long) i);
      newPost.setCreatedAt(LocalDateTime.now());
      newPost.setUpdatedAt(LocalDateTime.now());

      posts.add(PostEntity.toResponseDto(newPost));
    }

    return posts;
  }
}