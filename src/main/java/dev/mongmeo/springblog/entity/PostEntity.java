package dev.mongmeo.springblog.entity;

import dev.mongmeo.springblog.dto.post.PostCreateDto;
import dev.mongmeo.springblog.dto.post.PostResponseDto;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@Entity
public class PostEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50)
  private String title;

  private String content;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  public static PostResponseDto toResponseDto(PostEntity post) {
    return PostResponseDto.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }

  public static PostEntity fromRequestDto(PostCreateDto dto) {
    return PostEntity.builder()
        .title(dto.getTitle())
        .content(dto.getContent())
        .build();
  }
}
