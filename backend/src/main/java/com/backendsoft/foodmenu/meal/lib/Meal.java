package com.backendsoft.foodmenu.meal.lib;

import com.backendsoft.foodmenu.menu.lib.MenuMeal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "meals")
@Data @NoArgsConstructor @AllArgsConstructor
public class Meal {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private double price;
    private String imageUrl; // optionnel

    @ManyToOne
    private MealType type; // entrée, plat, dessert, boisson

    @OneToMany(mappedBy = "meal")
    private List<MenuMeal> menuMeals;
}
