package com.backendsoft.foodmenu.meal;

import com.backendsoft.foodmenu.menu.request.MealTypeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Meal")
public class MealDto {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl; // optionnel

    private MealTypeDto type;
}
