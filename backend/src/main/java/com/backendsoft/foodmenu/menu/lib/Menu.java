package com.backendsoft.foodmenu.menu.lib;

import com.backendsoft.foodmenu.order.OrderItem;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menus")
@Data @NoArgsConstructor @AllArgsConstructor
public class Menu {
    @Id
    private String id;
    private LocalDate date;

    private String title;
    private boolean active;
    private Integer maxMeals;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "menu")
    private List<MenuMeal> menuMeals;

    @OneToMany(mappedBy = "menu")
    private List<OrderItem> orderItems;
}
