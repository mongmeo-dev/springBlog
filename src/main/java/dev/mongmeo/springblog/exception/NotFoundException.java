package dev.mongmeo.springblog.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

  private String message;

  public NotFoundException(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
