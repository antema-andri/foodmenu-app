package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.handlers.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.backendsoft.foodmenu.order")
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(
            OrderNotFoundException exception,
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

    @ExceptionHandler(ExcessOrderException.class)
    public ResponseEntity<ErrorDto> handleException(
            ExcessOrderException exception,
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

    @ExceptionHandler(InvalidMealOrderException.class)
    public ResponseEntity<ErrorDto> handleException(
            InvalidMealOrderException exception,
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

    @ExceptionHandler(NoOrderToUpdateException.class)
    public ResponseEntity<ErrorDto> handleException(
            NoOrderToUpdateException exception,
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

}
