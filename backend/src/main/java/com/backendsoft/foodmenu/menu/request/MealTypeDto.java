package com.backendsoft.foodmenu.menu.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "MealType")
public class MealTypeDto {
    private String id;
    private String name; //STARTER, MAIN_COURSE, DESSERT, DRINK
    private String description;
}
