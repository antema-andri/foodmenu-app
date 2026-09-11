package com.backendsoft.foodmenu.menu.lib.ids;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MenuMealId implements Serializable {
    private String menuId;
    private String mealId;
}
