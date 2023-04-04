package com.cmlteam.cmltemplate.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class BadRequestErrorRenderer {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BadRequestError> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (ObjectError error : ex.getBindingResult().getAllErrors()) {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    }
    log.warn("Bad Request: " + errors);
    return ResponseEntity.badRequest().body(new BadRequestError(false, "Bad Request", errors));
  }
}
