package com.backendsoft.foodmenu.meal.lib;

import com.backendsoft.foodmenu.meal.MealDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MealValidator {

    private static final String ERROR_MEAL_NULL = "Meal information must be provided";
    private static final String ERROR_NAME_EMPTY = "Meal name must be provided";

    public static List<String> validate(MealDto mealDto) {
        List<String> errors = new ArrayList<>();

        if (mealDto == null) {
            errors.add(ERROR_MEAL_NULL);
            return errors;
        }

        if (!StringUtils.hasLength(mealDto.getName())) {
            errors.add(ERROR_NAME_EMPTY);
        }

        return errors;
    }
}
