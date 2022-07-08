package dev.mongmeo.springblog.exception;

import dev.mongmeo.springblog.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<ErrorResponseDto> validException(BindException e) {

    String message =
        e.getFieldError().getField() + " : " + e.getBindingResult().getAllErrors().get(0)
            .getDefaultMessage();

    ErrorResponseDto errorResponseDto = ErrorResponseDto.builder().code(400).message(message)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
  }

}
