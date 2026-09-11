package com.backendsoft.foodmenu.menu.request;

import lombok.Data;

@Data
public class MealUpdateRequest {
    private String name;
    private String description;
}
