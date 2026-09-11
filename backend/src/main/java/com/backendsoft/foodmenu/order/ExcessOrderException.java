package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class ExcessOrderException extends Exception {
    private ErrorCodes errorCode;

    public ExcessOrderException(String message) {
        super(message);
    }

    public ExcessOrderException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
