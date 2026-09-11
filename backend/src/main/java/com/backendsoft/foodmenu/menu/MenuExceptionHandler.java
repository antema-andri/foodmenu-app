package com.backendsoft.foodmenu.menu;

import com.backendsoft.foodmenu.customer.lib.CustomerNotFoundException;
import com.backendsoft.foodmenu.customer.lib.UndeletableCustomerException;
import com.backendsoft.foodmenu.handlers.ErrorDto;
import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;
import com.backendsoft.foodmenu.menu.exception.UndeletableMenuException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.backendsoft.foodmenu.menu")
public class MenuExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MenuNotFoundException.class)
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

    @ExceptionHandler(UndeletableMenuException.class)
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
