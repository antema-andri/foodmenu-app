package com.backendsoft.foodmenu.auth.lib;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
