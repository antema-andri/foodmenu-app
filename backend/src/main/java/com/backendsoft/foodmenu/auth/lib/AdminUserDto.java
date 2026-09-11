package com.backendsoft.foodmenu.auth.lib;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "AdminUser")
public class AdminUserDto {
    private String id;

    private String username;
    private String password;
    private String email;
    private String fullname;
}
