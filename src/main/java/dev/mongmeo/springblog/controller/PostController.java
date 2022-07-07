package dev.mongmeo.springblog.controller;

import dev.mongmeo.springblog.dto.PostResponseDto;
import dev.mongmeo.springblog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
