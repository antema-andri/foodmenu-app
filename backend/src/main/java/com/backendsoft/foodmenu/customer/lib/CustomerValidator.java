package com.backendsoft.foodmenu.customer.lib;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerValidator {

    private static final String ERROR_CUSTOMER_NULL = "Customer information must be provided";
    private static final String ERROR_FULLNAME_EMPTY = "Customer full name must be provided";

    public static List<String> validate(CustomerDto customerDto) {
        List<String> errors = new ArrayList<>();

        if (customerDto == null) {
            errors.add(ERROR_CUSTOMER_NULL);
            return errors;
        }

        if (!StringUtils.hasLength(customerDto.getFullname())) {
            errors.add(ERROR_FULLNAME_EMPTY);
        }

        return errors;
    }
}
