package com.backendsoft.foodmenu.meal.lib;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MealRepository extends JpaRepository<Meal, String> {
    @Query("SELECT m FROM Meal m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Meal> findMealsByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
