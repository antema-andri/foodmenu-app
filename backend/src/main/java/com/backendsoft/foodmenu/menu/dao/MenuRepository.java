package com.backendsoft.foodmenu.menu.dao;

import com.backendsoft.foodmenu.menu.lib.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, String> {
    List<Menu> findByActiveTrueOrderByDateDesc();

    Page<Menu> findByActiveTrueOrderByDateDesc(Pageable pageable);

    Page<Menu> findByOrderByDateDesc(Pageable pageable);

    Page<Menu> findByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Menu> findFirstByActiveTrueOrderByDateDesc();

//    @Query("""
//    SELECT m FROM Menu m
//    WHERE m.active = true
//      AND SIZE(m.menuMeals) > 0
//    ORDER BY m.date DESC
//    """)
//    Optional<Menu> findLatestActiveMenuWithMeals();

    @Query("""
    SELECT m FROM Menu m
    WHERE m.active = true
      AND SIZE(m.menuMeals) > 0
    ORDER BY m.createdAt DESC
    """)
    Optional<Menu> findLatestActiveMenuWithMeals();

    Optional<Menu> findFirstByActiveIsTrueAndMenuMealsIsNotEmptyOrderByCreatedAtDesc();
}
