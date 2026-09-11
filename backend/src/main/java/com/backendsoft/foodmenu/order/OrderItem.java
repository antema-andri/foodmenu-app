package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.customer.lib.Customer;
import com.backendsoft.foodmenu.meal.lib.Meal;
import com.backendsoft.foodmenu.menu.lib.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "order_items",
    uniqueConstraints = @UniqueConstraint(
            columnNames = {"customer_id", "menu_id", "meal_id"}
    )
)
@Data @NoArgsConstructor @AllArgsConstructor
public class OrderItem {
    @Id
    private String id;
    private int quantity; // par ex. 1 plat principal, 2 desserts...

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // pas besoin de login

    @ManyToOne
    @JoinColumn(name = "meal_id")
    private Meal meal; // meal du menu

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu; // menu de la commande
}
