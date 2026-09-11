package com.backendsoft.foodmenu.menu.request;

import com.backendsoft.foodmenu.meal.MealDto;
import lombok.Data;

@Data
public class MealOrderData {
    private MealDto meal;
    private int totalMealOrder;
}
