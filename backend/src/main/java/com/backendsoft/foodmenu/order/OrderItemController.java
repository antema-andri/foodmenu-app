package com.backendsoft.foodmenu.order;

import com.backendsoft.foodmenu.customer.lib.CustomerNotFoundException;
import com.backendsoft.foodmenu.meal.lib.MealNotFoundException;
import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;
import com.backendsoft.foodmenu.menu.response.NumberOderResp;
import com.backendsoft.foodmenu.utils.EntityNotFoundException;
import com.backendsoft.foodmenu.menu.lib.MenuService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/"})
public class OrderItemController {
    private final MenuService menuService;

    public OrderItemController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("orders/{menuId}/{customerId}")
    public OrderItemDto getCustomerOrderFromMenu(
        @PathVariable String menuId,
        @PathVariable String customerId
    ) throws OrderNotFoundException {
        return menuService.getCustmerOrderFromMenu(menuId,customerId);
    }

    @GetMapping("orders/orderStat/{menuId}")
    public NumberOderResp getNumberOrderStat(
        @PathVariable String menuId
    ) throws MenuNotFoundException {
        return menuService.getOrderNumber(menuId);
    }

    @PutMapping("orders")
    public OrderItemDto chooseMenu(@RequestBody OrderRequest orderRequest) throws InvalidMealOrderException, ExcessOrderException, MealNotFoundException, MenuNotFoundException, CustomerNotFoundException {
        return menuService.chooseMenu(orderRequest.getMenuId(),orderRequest.getCustomerId(),orderRequest.getMealId());
    }

    @PutMapping("orders/replace")
    public OrderItemDto changeOrder(@RequestBody OrderRequest orderRequest) throws NoOrderToUpdateException, MealNotFoundException, MenuNotFoundException {
        return menuService.replaceOrder(orderRequest.getMenuId(),orderRequest.getCustomerId(),orderRequest.getMealId());
    }

}
