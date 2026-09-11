package com.backendsoft.foodmenu.menu.exception;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class UndeletableMenuException extends Exception {
    private ErrorCodes errorCode;

    public UndeletableMenuException(String message) {
        super(message);
    }

    public UndeletableMenuException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
