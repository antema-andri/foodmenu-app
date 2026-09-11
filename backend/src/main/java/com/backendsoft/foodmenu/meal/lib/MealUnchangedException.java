package com.backendsoft.foodmenu.meal.lib;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class MealUnchangedException extends Exception {
    private ErrorCodes errorCode;

    public MealUnchangedException(String message) {
        super(message);
    }

    public MealUnchangedException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
