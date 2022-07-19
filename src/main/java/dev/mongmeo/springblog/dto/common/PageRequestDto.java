package dev.mongmeo.springblog.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestDto {

  @Schema(description = "페이지 번호")
  @Positive
  private Integer page;

  @Schema(description = "한 페이지에 표시될 게시물 수")
  @Positive
  private Integer size;

}
