package com.backendsoft.foodmenu.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    boolean existsByCustomerIdAndMenuIdAndMealId(String customerId, String menuId, String mealId);
    Optional<OrderItem> findByCustomerIdAndMenuIdAndMealId(String customerId, String menuId, String mealId);

    boolean existsByCustomerIdAndMenuId(String customerId, String menuId);
    Optional<OrderItem> findByCustomerIdAndMenuId(String customerId, String menuId);

    @Query("SELECT COUNT(DISTINCT oi.customer.id) " +
            "FROM OrderItem oi " +
            "WHERE oi.menu.id = :menuId")
    long countDistinctCustomersByMenu(@Param("menuId") String menuId);

    @Query("""
        SELECT COUNT(oi)
        FROM OrderItem oi
        WHERE oi.meal.id = :mealId
          AND oi.menu.id = :menuId
    """)
    long countLinesForMealInMenu(String menuId,String mealId);

    List<OrderItem> findByMenuId(String menuId);

    List<OrderItem> findByCustomerId(String customerId);

    @Query("SELECT COUNT(o) FROM OrderItem o WHERE o.menu.id = :menuId")
    long countOrderItemsByMenu(@Param("menuId") String menuId);
}
