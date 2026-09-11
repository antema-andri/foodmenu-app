package com.backendsoft.foodmenu.meal.mapper;

import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.meal.lib.Meal;
import com.backendsoft.foodmenu.meal.lib.MealType;
import com.backendsoft.foodmenu.menu.request.MealTypeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MealMapper {

    MealDto fromEntity(Meal meal);
    Meal fromDto(MealDto mealDto);

    MealTypeDto fromEntity(MealType mealType);
    MealType fromDto(MealTypeDto mealTypeDto);
}
