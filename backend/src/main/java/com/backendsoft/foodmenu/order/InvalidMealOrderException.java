package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.utils.ErrorCodes;

import java.util.List;

public class InvalidMealOrderException extends Exception {
    private List<String> errors;
    private ErrorCodes errorCode;

    public InvalidMealOrderException(String message) {
        super(message);
    }

    public InvalidMealOrderException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public InvalidMealOrderException(String message, ErrorCodes errorCode, List<String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
