package com.backendsoft.foodmenu.menu.request;

import com.backendsoft.foodmenu.meal.MealDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "MenuMeal")
public class MenuMealDto {
    private MenuDto menu;
    private MealDto meal;
}
