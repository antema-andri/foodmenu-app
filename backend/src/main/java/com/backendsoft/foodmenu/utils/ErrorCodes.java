package com.backendsoft.foodmenu.utils;

public enum ErrorCodes {
    MENU_NOT_FOUND(10000),
    MENU_NOT_VALID(10001),
    UNAUTHORIZED_DELETE(10002),

    MEAL_NOT_FOUND(20000),
    MEAL_NOT_VALID(20001),

    CUSTOMER_NOT_FOUND(30000),
    CUSTOMER_NOT_VALID(30001),

    ADMINUSER_NOT_FOUND(40000),
    ADMINUSER_NOT_VALID(40001);

    private int code;

    private ErrorCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
