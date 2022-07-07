package dev.mongmeo.springblog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

  @Schema(description = "http 상태코드", example = "404")
  private int code;

  @Schema(description = "에러 메세지", example = "Not Found")
  private String message;
}
