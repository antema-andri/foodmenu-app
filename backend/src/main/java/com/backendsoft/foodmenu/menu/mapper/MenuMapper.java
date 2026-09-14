package com.backendsoft.foodmenu.menu.mapper;

import com.backendsoft.foodmenu.meal.MealDto;
import com.backendsoft.foodmenu.meal.lib.Meal;
import com.backendsoft.foodmenu.menu.lib.Menu;
import com.backendsoft.foodmenu.menu.lib.MenuMeal;
import com.backendsoft.foodmenu.menu.request.MenuDto;
import com.backendsoft.foodmenu.menu.request.MenuMealDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    // ==============================================
    // MAPPINGS WITHOUT CONTEXT (default)
    // ==============================================

    @Named("default")
    @Mapping(target = "meals", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    MenuDto fromEntity(Menu menu);

    @Named("default")
    @Mapping(target = "menu", ignore = true)
    MenuMealDto fromEntity(MenuMeal menuMeal);

    @Named("default")
    MealDto fromEntity(Meal meal);

    Menu fromDto(MenuDto menuDto);

    @Mapping(target = "menu", ignore = true)
    MenuMeal fromDto(MenuMealDto menuMealDto);

    Meal fromDto(MealDto mealDto);

    // ==============================================
    // MAPPINGS WITH CONTEXT (cycle handling)
    // ==============================================

    @Named("withContext")
    @Mapping(target = "meals", source = "menuMeals", qualifiedByName = "menuMealToMealDto")
    @Mapping(target = "orderItems", ignore = true)
    MenuDto fromEntity(Menu menu, @Context CycleAvoidingMappingContext context);

    @Named("withContext")
    @Mapping(target = "menu", ignore = true)
    MenuMealDto fromEntity(MenuMeal menuMeal, @Context CycleAvoidingMappingContext context);

    @Named("withContext")
    MealDto fromEntity(Meal meal, @Context CycleAvoidingMappingContext context);

    @Named("withContextDto")
    Menu fromDto(MenuDto menuDto, @Context CycleAvoidingMappingContext context);

    @Named("withContextDto")
    @Mapping(target = "menu", ignore = true)
    MenuMeal fromDto(MenuMealDto menuMealDto, @Context CycleAvoidingMappingContext context);

    @Named("withContextDto")
    Meal fromDto(MealDto mealDto, @Context CycleAvoidingMappingContext context);

    // ==============================================
    // SINGLE ELEMENT CONVERSION METHOD
    // ==============================================

    @Named("menuMealToMealDto")
    default MealDto menuMealToMealDto(MenuMeal menuMeal, @Context CycleAvoidingMappingContext context) {
        if (menuMeal == null) {
            return null;
        }
        return fromEntity(menuMeal.getMeal(), context);
    }

}
