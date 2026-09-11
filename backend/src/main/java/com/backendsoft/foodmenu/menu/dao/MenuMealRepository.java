package com.backendsoft.foodmenu.menu.dao;

import com.backendsoft.foodmenu.menu.lib.MenuMeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuMealRepository extends JpaRepository<MenuMeal, String> {
    boolean existsByMenuIdAndMealId(String menuId, String mealId);
    Optional<MenuMeal> findByMenuIdAndMealId(String menuId, String mealId);
}
