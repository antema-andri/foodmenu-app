package com.backendsoft.foodmenu.menu.request;

import com.backendsoft.foodmenu.order.OrderItemDto;
import lombok.Data;

import java.util.List;

@Data
public class MenuOrderData {
    MenuDto menu;
    List<MealOrderData> mealOrderDatas;
    List<OrderItemDto> customerOrders;
    int totalOrders;
}
