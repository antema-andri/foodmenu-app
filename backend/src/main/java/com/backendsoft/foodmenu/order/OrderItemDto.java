package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.customer.lib.CustomerDto;
import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.menu.request.MenuDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "OrderItem")
public class OrderItemDto {
    private String id;
    private int quantity; // par ex. 1 plat principal, 2 desserts...

    private CustomerDto customer; // pas besoin de login
    private MealDto meal; // meal du menu
    private MenuDto menu; // menu de la commande
}
