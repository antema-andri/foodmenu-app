package com.backendsoft.foodmenu.menu;

import com.backendsoft.foodmenu.meal.lib.MealNotFoundException;
import com.backendsoft.foodmenu.meal.lib.MealUnchangedException;
import com.backendsoft.foodmenu.menu.exception.MenuNotFoundException;
import com.backendsoft.foodmenu.menu.exception.UndeletableMenuException;
import com.backendsoft.foodmenu.menu.lib.MenuService;
import com.backendsoft.foodmenu.menu.request.MenuDto;
import com.backendsoft.foodmenu.menu.request.MenuListUpdateRequest;
import com.backendsoft.foodmenu.menu.request.MenuPage;
import com.backendsoft.foodmenu.menu.request.MenuUpdateStatusRequest;
import com.backendsoft.foodmenu.menu.response.MealOrderTotalResponse;
import com.backendsoft.foodmenu.order.InvalidMealOrderException;
import com.backendsoft.foodmenu.utils.InvalidEntityException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/"})
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("menus")
    public MenuDto saveMenu(@RequestBody MenuDto menuDto) throws MenuNotFoundException, InvalidEntityException {
        return menuService.save(menuDto);
    }

    @GetMapping("menus")
    public MenuPage getMenus(
        @RequestParam(value = "page",defaultValue = "0") int page,
        @RequestParam(value = "size",defaultValue = "10") int size
    ) {
        return menuService.getMenus(page,size);
    }

    @GetMapping("menus/lastActive")
    public MenuDto getLatestActiveMenu() throws MenuNotFoundException {
        return menuService.getLatestActiveMenu();
    }

    @GetMapping("menus/{menuId}")
    public MenuDto getMenu(@PathVariable String menuId) throws MenuNotFoundException {
        return menuService.getMenu(menuId);
    }

    @GetMapping("/menus/{menuId}/meals/{mealId}/order-number")
    @Operation(summary = "Récupérer le nombre de commande d'un repas dans un menu")
    public MealOrderTotalResponse getOrderNumberOfMeal(
            @PathVariable String menuId,
            @PathVariable String mealId
    ) throws MealNotFoundException, MenuNotFoundException {
        return menuService.getOrderNumberOfMeal(menuId,mealId);
    }

    @PutMapping("menus/{menuId}")
    public MenuDto updateMenuMeal(
            @PathVariable String menuId,
            @RequestBody MenuListUpdateRequest menuListUpdateRequest
            ) throws InvalidMealOrderException, MealUnchangedException, MealNotFoundException {
        return menuService.changeMealList(menuId,menuListUpdateRequest.getCurrentMealId(),menuListUpdateRequest.getNewMealId());
    }

    @DeleteMapping("menus/{menuId}")
    public void deleteMenu(@PathVariable String menuId) throws UndeletableMenuException, MenuNotFoundException {
        menuService.deleteMenu(menuId);
    }

    @PutMapping("menus/{menuId}/status")
    @Operation(summary = "Changer le status du menu")
    public MenuDto updateMenuStatus(
            @PathVariable String menuId,
            @RequestBody MenuUpdateStatusRequest menuStatusReq
    ) throws MenuNotFoundException {
        return menuService.validateMenu(menuId, menuStatusReq);
    }
}
