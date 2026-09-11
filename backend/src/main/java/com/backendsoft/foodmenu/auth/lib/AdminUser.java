package com.backendsoft.foodmenu.auth.lib;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adminUsers")
@Data @NoArgsConstructor @AllArgsConstructor
public class AdminUser {
    @Id
    private String id;

    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String fullname;
}
