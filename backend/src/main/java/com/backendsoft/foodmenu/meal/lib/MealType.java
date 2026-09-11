package com.backendsoft.foodmenu.meal.lib;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class MealType {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String name; //STARTER, MAIN_COURSE, DESSERT, DRINK
    private String description;
}
