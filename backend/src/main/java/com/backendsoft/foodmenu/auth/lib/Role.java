package com.backendsoft.foodmenu.auth.lib;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Role {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name; // "ADMIN", "EMPLOYEE"
}
