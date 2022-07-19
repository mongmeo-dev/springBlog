package dev.mongmeo.springblog.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDto {

  @Schema(description = "게시물 제목", example = "게시물 제목입니다.", required = true)
  @NotBlank
  @Size(max = 50)
  private String title;

  @Schema(description = "게시물 본문", example = "게시물 내용입니다.", required = true)
  @NotBlank
  private String content;

}
