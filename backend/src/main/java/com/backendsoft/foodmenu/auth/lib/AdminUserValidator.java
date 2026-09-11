package com.backendsoft.foodmenu.auth.lib;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdminUserValidator {

    private static final String ERROR_ADMIN_USER_NULL = "Admin user information must be provided";
    private static final String ERROR_FULLNAME_EMPTY = "Admin user full name must be provided";

    public static List<String> validate(AdminUserDto adminUserDto) {
        List<String> errors = new ArrayList<>();

        if (adminUserDto == null) {
            errors.add(ERROR_ADMIN_USER_NULL);
            return errors;
        }

        if (!StringUtils.hasLength(adminUserDto.getFullname())) {
            errors.add(ERROR_FULLNAME_EMPTY);
        }

        return errors;
    }

}
