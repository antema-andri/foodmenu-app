package com.backendsoft.foodmenu.auth.dao;

import com.backendsoft.foodmenu.auth.lib.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
