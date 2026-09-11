package com.backendsoft.foodmenu.meal.lib;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class UndeletableMealException extends Exception {
    private ErrorCodes errorCode;

    public UndeletableMealException(String message) {
        super(message);
    }

    public UndeletableMealException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
