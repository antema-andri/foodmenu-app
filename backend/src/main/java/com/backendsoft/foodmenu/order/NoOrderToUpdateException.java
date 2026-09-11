package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class NoOrderToUpdateException extends Exception {
    private ErrorCodes errorCode;

    public NoOrderToUpdateException(String message) {
        super(message);
    }

    public NoOrderToUpdateException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
