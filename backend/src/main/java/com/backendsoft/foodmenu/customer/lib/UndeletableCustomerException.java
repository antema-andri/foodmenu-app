package com.backendsoft.foodmenu.customer.lib;

import com.backendsoft.foodmenu.utils.ErrorCodes;

public class UndeletableCustomerException extends Exception {
    private ErrorCodes errorCode;

    public UndeletableCustomerException(String message) {
        super(message);
    }

    public UndeletableCustomerException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return this.errorCode;
    }
}
