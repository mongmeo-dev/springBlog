package dev.mongmeo.springblog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.mongmeo.springblog.dto.comment.CommentResponseDto;
import dev.mongmeo.springblog.entity.CommentEntity;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.exception.NotFoundException;
import dev.mongmeo.springblog.service.CommentService;
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

@WebMvcTest(CommentController.class)
@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private CommentService commentService;

  @Test
  @DisplayName("모든 댓글 리스트를 json 형식의 commentResponseDto 리스트로 받아야 함")
  void getAllCommentsTest() throws Exception {
    // given
    Mockito.when(commentService.getAllCommentsByPostId(anyLong(), any(), any()))
        .thenReturn(createDummyComments());

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1/comments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].id").exists())
        .andExpect(jsonPath("$[*].content").exists())
        .andExpect(jsonPath("$[*].createdAt").exists())
        .andExpect(jsonPath("$[*].updatedAt").exists())
        .andExpect(jsonPath("$.length()").value(10))
        .andDo(print());

  }

  @Test
  @DisplayName("페이지 쿼리스트링에 숫자가 아닌 값을 넘기면 상태코드 400을 내려줘야 함")
  void getCommentsWithPageBindingExceptionTest() throws Exception {
    // when, then
    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/posts/1/comments").queryParam("size", "a")
                .queryParam("page", "b"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  @DisplayName("페이지 쿼리스트링에 양수가 아닌 값을 넘기면 상태코드 400을 내려줘야 함")
  void getCommentsWithPageValidationExceptionTest() throws Exception {
    // when, then
    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/posts/1/comments").queryParam("size", "0")
                .queryParam("page", "-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  @DisplayName("특정 게시물에 달린 댓글 수를 반환해야 함")
  void getCommentsCountTest() throws Exception {
    // given
    Mockito.when(commentService.getCommentsCountByPostId(anyLong())).thenReturn(5L);

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1/comments/count"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.count").value(5));
  }

  @Test
  @DisplayName("댓글 수 조회시 게시물이 존재하지 않으면 상태코드 404로 응답해야 함")
  void getCommentsCountPostNotFoundTest() throws Exception {
    // given
    Mockito.when(commentService.getCommentsCountByPostId(anyLong()))
        .thenThrow(NotFoundException.class);

    // when, then
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/1/comments/count"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(404));
  }

  private List<CommentResponseDto> createDummyComments() {
    List<CommentResponseDto> comments = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      CommentEntity newComment = CommentEntity.builder()
          .id((long) i)
          .content("content" + i)
          .post(new PostEntity())
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();

      comments.add(CommentEntity.toResponse(newComment));
    }

    return comments;
  }
}