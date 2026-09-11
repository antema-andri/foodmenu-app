package com.backendsoft.foodmenu.menu.lib;

import com.backendsoft.foodmenu.meal.lib.Meal;
import com.backendsoft.foodmenu.menu.lib.ids.MenuMealId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_meals")
@Data @NoArgsConstructor @AllArgsConstructor
public class MenuMeal {
    @EmbeddedId
    private MenuMealId id = new MenuMealId();

    @ManyToOne
    @MapsId("menuId")
    private Menu menu;
    @ManyToOne
    @MapsId("mealId")
    private Meal meal;
}
