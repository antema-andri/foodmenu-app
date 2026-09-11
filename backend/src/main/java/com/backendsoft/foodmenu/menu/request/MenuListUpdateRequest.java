package com.backendsoft.foodmenu.menu.request;

import lombok.Data;

@Data
public class MenuListUpdateRequest {
    private String currentMealId;
    private String newMealId;
}
