package dev.mongmeo.springblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@NotBlank
@Getter
@Builder
public class PostResponseDto {

  @Schema(description = "게시물 제목", example = "게시물 제목입니다.")
  private String title;

  @Schema(description = "게시물 본문", example = "게시물 내용입니다.")
  private String content;

  @Schema(description = "게시물 생성 시간", example = "2022-07-07T10:00:00.0000")
  private LocalDateTime createdAt;

  @Schema(description = "게시물 업데이트 시간", example = "2022-07-07T10:00:00.0000")
  private LocalDateTime updatedAt;

}
