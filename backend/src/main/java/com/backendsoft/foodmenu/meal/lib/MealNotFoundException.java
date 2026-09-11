package com.backendsoft.foodmenu.meal.lib;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class MealNotFoundException extends Exception {
    private ErrorCodes errorCode;

    public MealNotFoundException(String message) {
        super(message);
    }

    public MealNotFoundException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
