package dev.mongmeo.springblog.controller;

import dev.mongmeo.springblog.dto.comment.CommentResponseDto;
import dev.mongmeo.springblog.dto.common.ErrorResponseDto;
import dev.mongmeo.springblog.dto.common.PageRequestDto;
import dev.mongmeo.springblog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "댓글에 관련된 컨트롤러")
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "특정 게시물의 모든 댓글 또는 요청한 페이지의 댓글 리스트 가져오기")
  @ApiResponse(responseCode = "200", description = "특정 게시물의 모든 댓글 리스트 반환 혹은 요청한 페이지의 댓글 리스트 반환")
  @ApiResponse(responseCode = "400", description = "페이지 요청에 잘못된 값이 들어옴")
  @ApiResponse(responseCode = "404", description = "게시물이 존재하지 않음", content = {
      @Content(schema = @Schema(implementation = ErrorResponseDto.class))})
  @GetMapping
  public List<CommentResponseDto> getAllCommentsByPostId(
      @Parameter(description = "게시물 id") @PathVariable("postId") long postId,
      @ModelAttribute @Valid PageRequestDto dto) {
    return commentService.getAllCommentsByPostId(postId, dto.getPage(), dto.getSize());
  }

  @Operation(summary = "게시물의 모든 댓글 수 가져오기")
  @ApiResponse(responseCode = "200", description = "게시물의 모든 댓글 수 반환", content = {
      @Content(schema = @Schema(example = "{\"count\": \"7\"}"))})
  @ApiResponse(responseCode = "404", description = "게시물이 존재하지 않음", content = {
      @Content(schema = @Schema(implementation = ErrorResponseDto.class))})
  @GetMapping("/count")
  public ResponseEntity<String> getCommentsCountByPostId(
      @Parameter(description = "게시물 id") @PathVariable("postId") long postId) {
    long count = commentService.getCommentsCountByPostId(postId);

    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
        .body("{\"count\": \"" + count + "\"}");
  }
}
