package com.backendsoft.foodmenu.meal;

import com.backendsoft.foodmenu.meal.lib.MealNotFoundException;
import com.backendsoft.foodmenu.meal.lib.MealService;
import com.backendsoft.foodmenu.meal.lib.MealUnchangedException;
import com.backendsoft.foodmenu.meal.lib.UndeletableMealException;
import com.backendsoft.foodmenu.menu.request.MealPage;
import com.backendsoft.foodmenu.menu.request.MealUpdateRequest;
import com.backendsoft.foodmenu.utils.InvalidEntityException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/"})
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping({"meals"})
    public MealDto save(@RequestBody MealDto mealDto) throws InvalidEntityException {
        return mealService.save(mealDto);
    }

    @GetMapping("meals")
    public MealPage getMealsByKeyword(
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size
    ) {
        return mealService.getMeals(keyword,page,size);
    }

    @GetMapping("meals/{mealId}")
    public MealDto getMeal(@PathVariable String mealId) throws MealNotFoundException {
        return mealService.getMeal(mealId);
    }

    @PutMapping("meals/{mealId}")
    public MealDto updateMeal(
            @PathVariable String mealId,
            @RequestBody MealUpdateRequest updateRequest
    ) throws MealNotFoundException, MealUnchangedException {
        return mealService.updateMeal(mealId, updateRequest.getName(), updateRequest.getDescription());
    }

    @DeleteMapping("meals/{mealId}")
    public void deleteMeal(@PathVariable String mealId) throws MealNotFoundException, UndeletableMealException {
        mealService.deleteMeal(mealId);
    }
}
