package com.backendsoft.foodmenu.customer;

import com.backendsoft.foodmenu.customer.lib.CustomerNotFoundException;
import com.backendsoft.foodmenu.customer.lib.CustomerUnchangedException;
import com.backendsoft.foodmenu.customer.lib.UndeletableCustomerException;
import com.backendsoft.foodmenu.handlers.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.backendsoft.foodmenu.customer")
public class CustomerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(
            CustomerNotFoundException exception,
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

    @ExceptionHandler(CustomerUnchangedException.class)
    public ResponseEntity<ErrorDto> handleException(
            CustomerUnchangedException exception,
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

    @ExceptionHandler(UndeletableCustomerException.class)
    public ResponseEntity<ErrorDto> handleException(
            UndeletableCustomerException exception,
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
