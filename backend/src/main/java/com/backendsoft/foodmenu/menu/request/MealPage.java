package com.backendsoft.foodmenu.menu.request;

import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.utils.Page;
import lombok.Data;

import java.util.List;

@Data
public class MealPage extends Page {
    private List<MealDto> meals;
}
