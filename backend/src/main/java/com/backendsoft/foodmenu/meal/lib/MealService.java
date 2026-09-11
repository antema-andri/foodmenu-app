package com.backendsoft.foodmenu.meal.lib;

import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.menu.request.MealPage;
import com.backendsoft.foodmenu.utils.InvalidEntityException;

public interface MealService {
    MealDto save(MealDto mealDto) throws InvalidEntityException;
    MealDto getMeal(String mealId) throws MealNotFoundException;
    MealPage getMeals(String keyword, int page, int size);
    MealDto updateMeal(String mealId, String name, String description) throws MealNotFoundException, MealUnchangedException;
    void deleteMeal(String mealId) throws MealNotFoundException, UndeletableMealException;
}
