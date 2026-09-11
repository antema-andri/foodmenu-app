package com.backendsoft.foodmenu.customer.lib;

import com.backendsoft.foodmenu.utils.ErrorCodes;

import java.util.List;

public class CustomerNotFoundException extends Exception {
    private List<String> errors;
    private ErrorCodes errorCode;

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public CustomerNotFoundException(String message, ErrorCodes errorCode, List<String> errors) {
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
