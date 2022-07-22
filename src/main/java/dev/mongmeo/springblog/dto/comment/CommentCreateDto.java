package dev.mongmeo.springblog.dto.comment;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDto {

  @NotBlank
  private String content;
}
