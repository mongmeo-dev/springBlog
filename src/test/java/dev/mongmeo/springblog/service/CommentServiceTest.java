package dev.mongmeo.springblog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.mongmeo.springblog.dto.comment.CommentCreateDto;
import dev.mongmeo.springblog.dto.comment.CommentResponseDto;
import dev.mongmeo.springblog.entity.CommentEntity;
import dev.mongmeo.springblog.entity.PostEntity;
import dev.mongmeo.springblog.repository.CommentRepository;
import dev.mongmeo.springblog.repository.PostRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CommentServiceTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  CommentService commentService;

  @Test
  @DisplayName("특정 게시물에 달린 모든 댓글 리스트를 반환해야 함")
  void getAllCommentTest() {
    // given
    long postId = createDummyCommentsAndPostAndReturnPostId();

    // when
    List<CommentResponseDto> allComments = commentService.getAllCommentsByPostId(postId, null,
        null);

    // then
    assertEquals(10, allComments.size());
  }

  @Test
  @DisplayName("페이지 정보와 함께 호출시 해당 페이지의 게시물 리스트를 반환해야 함")
  void getAllPostWithPage() {
    // given
    long postId = createDummyCommentsAndPostAndReturnPostId();

    // when
    List<CommentResponseDto> pageComments = commentService.getAllCommentsByPostId(postId, 1, 3);

    // then
    assertEquals(3, pageComments.size());
  }

  @Test
  @DisplayName("특정 게시물에 달린 모든 댓글 수를 반환해야 함.")
  void getCommentsCountByPostId() {
    // given
    long postId = createDummyCommentsAndPostAndReturnPostId();

    // when
    long commentsCount = commentService.getCommentsCountByPostId(postId);

    // then
    assertEquals(10, commentsCount);
  }

  @Test
  @DisplayName("새 댓글이 등록되어야 함")
  void createCommentTest() {
    // given
    long postId = createDummyCommentsAndPostAndReturnPostId();
    CommentCreateDto dto = new CommentCreateDto();
    dto.setContent("test content");

    // when
    CommentResponseDto savedComment = commentService.createComment(postId, dto);

    // then
    CommentEntity foundComment = commentRepository.findById(savedComment.getId()).get();
    assertEquals(savedComment.getContent(), foundComment.getContent());
  }

  private long createDummyCommentsAndPostAndReturnPostId() {
    PostEntity newPost = PostEntity.builder().title("title").content("content").build();
    PostEntity savedPost = postRepository.save(newPost);

    for (int i = 0; i < 10; i++) {
      CommentEntity newComment = CommentEntity.builder().content("content" + i).post(savedPost)
          .build();

      commentRepository.save(newComment);
    }

    return savedPost.getId();
  }
}