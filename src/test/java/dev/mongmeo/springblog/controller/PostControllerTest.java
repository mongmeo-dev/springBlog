package dev.mongmeo.springblog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mongmeo.springblog.dto.PostCreateDto;
import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.dto.PostUpdateDto;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.exception.NotFoundException;
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
import org.springframework.http.MediaType;
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
    Mockito.when(postService.getAllPosts(any(), any())).thenReturn(createDummyPosts());

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

  @Test
  @DisplayName("페이지 쿼리스트링에 숫자가 아닌 값을 넘기면 상태코드 400을 내려줘야 함")
  void getPostWithPageExceptionTest() throws Exception {
    // when, then
    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/posts").queryParam("size", "a").queryParam("page", "b"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  @DisplayName("모든 게시물 수를 반환받아야 함")
  void getPostCount() throws Exception {
    // given
    Mockito.when(postService.getPostsCount()).thenReturn(10L);

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/count"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.count").value(10));
  }

  @Test
  @DisplayName("PathVariable로 id를 전달하면 포스트 하나를 받아야 함")
  void getPostByIdTest() throws Exception {
    // given
    Mockito.when(postService.getPostById(anyLong())).thenReturn(createDummyPosts().get(0));

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").exists())
        .andExpect(jsonPath("$.content").exists())
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.updatedAt").exists())
        .andDo(print());
  }

  @Test
  @DisplayName("PathVariable로 전달한 id를 가진 게시물이 없다면 상태코드 404를 내려줘야 함")
  void getPostByIdExceptionTest() throws Exception {
    // given
    Mockito.when(postService.getPostById(anyLong())).thenThrow(NotFoundException.class);

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/100"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404"))
        .andExpect(jsonPath("$.message").exists())
        .andDo(print());
  }

  @Test
  @DisplayName("게시물을 만들어 반환해야 함")
  void createPostTest() throws Exception {
    // given
    PostCreateDto dto = PostCreateDto.builder()
        .title("test title")
        .content("test content")
        .build();

    PostResponseDto response = PostResponseDto.builder()
        .id(1L)
        .title(dto.getTitle())
        .content(dto.getContent())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    Mockito.when(postService.createPost(any())).thenReturn(response);

    // when, then
    ObjectMapper objectMapper = new ObjectMapper();
    String dtoJson = objectMapper.writeValueAsString(dto);
    mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJson)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value("test title"))
        .andExpect(jsonPath("$.content").value("test content"))
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.updatedAt").exists())
        .andDo(print());
  }

  @Test
  @DisplayName("validation에 실패하면 상태코드 400을 내려줘야 함")
  void createPostValidationTest() throws Exception {
    // given
    PostCreateDto dto = PostCreateDto.builder()
        .title("test title test title test title test title test title test title test title test ")
        .content("test content")
        .build();

    // when, then
    ObjectMapper objectMapper = new ObjectMapper();
    String dtoJson = objectMapper.writeValueAsString(dto);
    mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJson)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").exists())
        .andExpect(jsonPath("$.message").exists())
        .andDo(print());
  }

  @Test
  @DisplayName("요청에 따라 업데이트 된 게시물을 반환해야 함")
  void updatePostTest() throws Exception {
    // given
    PostUpdateDto dto = PostUpdateDto.builder()
        .title("update test title")
        .content("update test content")
        .build();

    PostResponseDto response = PostResponseDto.builder()
        .id(1L)
        .title(dto.getTitle())
        .content(dto.getContent())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    Mockito.when(postService.updatePost(anyLong(), any())).thenReturn(response);

    // when, then
    ObjectMapper objectMapper = new ObjectMapper();
    String dtoJson = objectMapper.writeValueAsString(dto);
    mockMvc.perform(
            MockMvcRequestBuilders.put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJson)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.title").value(dto.getTitle()))
        .andExpect(jsonPath("$.content").value(dto.getContent()))
        .andExpect(jsonPath("$.createdAt").exists())
        .andExpect(jsonPath("$.updatedAt").exists());
  }

  @Test
  @DisplayName("업데이트 시도시 PathVariable로 전달한 id를 가진 게시물이 없다면 상태코드 404를 내려줘야 함")
  void updatePostExceptionTest() throws Exception {
    // given
    Mockito.when(postService.updatePost(anyLong(), any())).thenThrow(NotFoundException.class);

    // when, then
    ObjectMapper objectMapper = new ObjectMapper();
    String dtoJson = objectMapper.writeValueAsString(new PostUpdateDto());
    mockMvc.perform(
            MockMvcRequestBuilders.put("/api/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dtoJson)
        )
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("404"))
        .andExpect(jsonPath("$.message").exists())
        .andDo(print());
  }

  @Test
  @DisplayName("삭제 성공시 상태코드 200과 '삭제 완료' 문구를 응답으로 내려줘야 함")
  void deletePostTest() throws Exception {
    // when, then
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("삭제 완료"));
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