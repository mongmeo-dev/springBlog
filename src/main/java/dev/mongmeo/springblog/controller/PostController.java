package dev.mongmeo.springblog.controller;

import dev.mongmeo.springblog.dto.ErrorResponseDto;
import dev.mongmeo.springblog.dto.PostCreateDto;
import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.dto.PostUpdateDto;
import dev.mongmeo.springblog.exception.NotFoundException;
import dev.mongmeo.springblog.service.PostService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "게시물에 관련된 컨트롤러")
@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;

  @Operation(summary = "모든 게시물 가져오기")
  @ApiResponse(responseCode = "200", description = "모든 게시물 리스트 반환")
  @GetMapping
  public List<PostResponseDto> getAllPosts() {
    return postService.getAllPosts();
  }

  @Operation(summary = "아이디로 게시물 단건 조회")
  @ApiResponse(responseCode = "200", description = "게시물 반환")
  @ApiResponse(
      responseCode = "404",
      description = "입력받은 id를 가진 게시물을 찾을 수 없음",
      content = {@Content(schema = @Schema(oneOf = {ErrorResponseDto.class}))})
  @GetMapping("/{id}")
  public PostResponseDto getPostById(
      @Parameter(description = "게시물 id") @PathVariable("id") long id
  ) {
    return postService.getPostById(id);
  }

  @Operation(summary = "게시물 등록하기")
  @ApiResponse(responseCode = "200", description = "게시물 등록 완료")
  @ApiResponse(
      responseCode = "400",
      description = "게시물 validation 실패",
      content = {@Content(schema = @Schema(oneOf = {ErrorResponseDto.class}))})
  @PostMapping
  public PostResponseDto createPost(
      @Parameter @RequestBody @Valid PostCreateDto dto) {
    return postService.createPost(dto);
  }

  @Operation(summary = "아이디로 게시물 업데이트")
  @ApiResponse(responseCode = "200", description = "업데이트 후 게시물 반환")
  @ApiResponse(
      responseCode = "404",
      description = "입력받은 id를 가진 게시물을 찾을 수 없음",
      content = {@Content(schema = @Schema(oneOf = {ErrorResponseDto.class}))})
  @PutMapping("/{id}")
  public PostResponseDto updatePost(
      @Parameter(description = "게시물 id") @PathVariable("id") long id,
      @Parameter(description = "게시물 업데이트 내용") @RequestBody PostUpdateDto dto
  ) {
    return postService.updatePost(id, dto);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> validException(MethodArgumentNotValidException e) {
    String message = e.getFieldError().getField()
        + " : "
        + e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .code(400)
        .message(message)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDto> notFoundException(NotFoundException e) {
    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
        .code(404)
        .message("게시물을 찾을 수 없습니다.")
        .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
  }
}
