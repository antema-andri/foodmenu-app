package com.backendsoft.foodmenu.menu.request;

import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.order.OrderItemDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "Menu")
public class MenuDto {
    private String id;
    private LocalDate date;

    private String title; // ex: "Menu du Mardi"
    private boolean active; // le menu actuel
    private Integer maxMeals;

    private LocalDateTime createdAt;

    private List<MealDto> meals;;
    @JsonIgnore
    private List<OrderItemDto> orderItems;
}
