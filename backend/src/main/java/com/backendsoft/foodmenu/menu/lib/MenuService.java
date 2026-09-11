package com.backendsoft.foodmenu.menu.lib;

import com.backendsoft.foodmenu.customer.lib.CustomerNotFoundException;
import com.backendsoft.foodmenu.meal.lib.MealNotFoundException;
import com.backendsoft.foodmenu.meal.lib.MealUnchangedException;
import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;
import com.backendsoft.foodmenu.menu.exception.UndeletableMenuException;
import com.backendsoft.foodmenu.menu.request.MenuDto;
import com.backendsoft.foodmenu.menu.request.MenuPage;
import com.backendsoft.foodmenu.menu.request.MenuUpdateStatusRequest;
import com.backendsoft.foodmenu.menu.response.MealOrderTotalResponse;
import com.backendsoft.foodmenu.menu.response.NumberOderResp;
import com.backendsoft.foodmenu.order.*;
import com.backendsoft.foodmenu.utils.InvalidEntityException;

public interface MenuService {
    MenuDto save(MenuDto menuDto) throws MenuNotFoundException, InvalidEntityException;
    MenuPage getMenus(int page, int size);
    MenuDto getMenu(String menuId) throws MenuNotFoundException;
    OrderItemDto chooseMenu(String menuId, String customerId, String mealId) throws MenuNotFoundException, CustomerNotFoundException, MealNotFoundException, InvalidMealOrderException, ExcessOrderException;
    OrderItemDto replaceOrder(String menuId, String customerId, String mealId) throws NoOrderToUpdateException, MenuNotFoundException, MealNotFoundException;
    MenuDto changeMealList(String menuId, String currentMealId, String newMealId) throws InvalidMealOrderException, MealNotFoundException, MealUnchangedException;
    OrderItemDto getCustmerOrderFromMenu(String menuId, String customerId) throws OrderNotFoundException;
    MenuDto getLatestActiveMenu() throws MenuNotFoundException;
    NumberOderResp getOrderNumber(String menuId) throws MenuNotFoundException;
    MealOrderTotalResponse getOrderNumberOfMeal(String menuId, String mealId) throws MealNotFoundException, MenuNotFoundException;
    void deleteMenu(String menuId) throws MenuNotFoundException, UndeletableMenuException;
    MenuDto validateMenu(String menuId, MenuUpdateStatusRequest menuUpdateStatusRequest) throws MenuNotFoundException;
}
