package com.backendsoft.foodmenu.auth.dao;

import com.backendsoft.foodmenu.auth.lib.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
    @Query("SELECT a FROM AdminUser a WHERE (a.username = :usernameOrEmail OR a.email = :usernameOrEmail)")
    Optional<AdminUser> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
}

