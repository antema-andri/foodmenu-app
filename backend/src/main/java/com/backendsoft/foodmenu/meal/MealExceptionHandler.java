package com.backendsoft.foodmenu.meal;

import com.backendsoft.foodmenu.handlers.ErrorDto;
import com.backendsoft.foodmenu.meal.lib.MealNotFoundException;
import com.backendsoft.foodmenu.meal.lib.MealUnchangedException;
import com.backendsoft.foodmenu.meal.lib.UndeletableMealException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.backendsoft.foodmenu.meal")
public class MealExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MealNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(
            MealNotFoundException exception,
            WebRequest webRequest
    ) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(status.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, status);
    }

    @ExceptionHandler(MealUnchangedException.class)
    public ResponseEntity<ErrorDto> handleException(
            MealUnchangedException exception,
            WebRequest webRequest
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(status.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, status);
    }

    @ExceptionHandler(UndeletableMealException.class)
    public ResponseEntity<ErrorDto> handleException(
            UndeletableMealException exception,
            WebRequest webRequest
    ) {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ErrorDto errorDto = ErrorDto.builder()
                .code(exception.getErrorCode())
                .httpCode(status.value())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDto, status);
    }

}
