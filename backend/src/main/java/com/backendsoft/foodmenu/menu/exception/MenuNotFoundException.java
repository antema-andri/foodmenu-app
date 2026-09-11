package com.backendsoft.foodmenu.menu.exception;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class MenuNotFoundException extends Exception {
    private ErrorCodes errorCode;

    public MenuNotFoundException(String message) {
        super(message);
    }

    public MenuNotFoundException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
