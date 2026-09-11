package com.backendsoft.foodmenu.handlers;

import com.backendsoft.foodmenu.utils.InvalidEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

   @ExceptionHandler({InvalidEntityException.class})
   public ResponseEntity<ErrorDto> handleException(InvalidEntityException exception, WebRequest webRequest) {
      HttpStatus badRequest = HttpStatus.BAD_REQUEST;
      ErrorDto errorDto = ErrorDto.builder().code(exception.getErrorCode()).httpCode(badRequest.value()).message(exception.getMessage()).errors(exception.getErrors()).build();
      return new ResponseEntity(errorDto, badRequest);
   }

}
