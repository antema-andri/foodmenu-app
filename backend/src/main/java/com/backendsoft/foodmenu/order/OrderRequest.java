package com.backendsoft.foodmenu.order;

import lombok.Data;

@Data
public class OrderRequest {
    private String menuId;
    private String customerId;
    private String mealId;
}
