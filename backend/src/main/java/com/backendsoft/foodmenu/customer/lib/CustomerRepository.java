package com.backendsoft.foodmenu.customer.lib;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT c FROM Customer c WHERE LOWER(c.fullname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Customer> findCustomersByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
