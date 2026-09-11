package com.backendsoft.foodmenu.menu.lib;

import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.menu.request.MenuDto;
import com.backendsoft.foodmenu.menu.request.MenuMealDto;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MenuValidator {
    private static final String ERROR_MENU_NULL = "Menu information must be provided";
    private static final String ERROR_TITLE_EMPTY = "Menu title must be provided";
    private static final String ERROR_DATE_PAST = "Menu date must be today or in the future";
    private static final String ERROR_MEALS_EMPTY = "At least one meal must be provided for the menu";
    private static final String ERROR_MEALS_DUPLICATE = "Each meal in the menu must be unique";

    public static List<String> validate(MenuDto menuDto) {
        List<String> errors = new ArrayList<>();

        if (menuDto == null) {
            errors.add(ERROR_MENU_NULL);
            return errors;
        }

        if (!StringUtils.hasLength(menuDto.getTitle())) {
            errors.add(ERROR_TITLE_EMPTY);
        }

        if (menuDto.getDate().isBefore(LocalDate.now())) {
            errors.add(ERROR_DATE_PAST);
        }

        if (isMealListEmptyOrInvalid(menuDto.getMeals())) {
            errors.add(ERROR_MEALS_EMPTY);
        } else if (hasDuplicateMeals(menuDto.getMeals())) {
            errors.add(ERROR_MEALS_DUPLICATE);
        }

        return errors;
    }

    private static boolean isMealListEmptyOrInvalid(List<MealDto> meals) {
        if (meals == null || meals.isEmpty()) {
            return true;
        }
        return meals.stream().anyMatch(m ->
                m == null || m.getId() == null
        );
    }

    private static boolean hasDuplicateMeals(List<MealDto> meals) {
        Set<Object> mealIds = new HashSet<>();
        for (MealDto m : meals) {
            if (m != null && m.getId() != null) {
                if (!mealIds.add(m.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
