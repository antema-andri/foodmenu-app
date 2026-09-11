package com.backendsoft.foodmenu.auth.lib;

import com.backendsoft.foodmenu.utils.InvalidEntityException;

public interface AdminUserService {
    AdminUserDto save(AdminUserDto adminUserDto) throws InvalidEntityException;
}
