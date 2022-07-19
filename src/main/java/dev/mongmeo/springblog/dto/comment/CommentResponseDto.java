package dev.mongmeo.springblog.dto.comment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

  long id;

  private String content;
  
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}

