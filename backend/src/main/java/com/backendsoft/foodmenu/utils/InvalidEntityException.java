package com.backendsoft.foodmenu.utils;

import java.util.List;

public class InvalidEntityException extends Exception {
    private List<String> errors;
    private ErrorCodes errorCode;

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public InvalidEntityException(String message, ErrorCodes errorCode, List<String> errors) {
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
