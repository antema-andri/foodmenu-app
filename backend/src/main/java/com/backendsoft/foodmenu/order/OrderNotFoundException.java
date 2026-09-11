package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class OrderNotFoundException extends Exception {
    private ErrorCodes errorCode;

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
