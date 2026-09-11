package com.backendsoft.foodmenu.auth.mapper;

import com.backendsoft.foodmenu.auth.lib.AdminUser;
import com.backendsoft.foodmenu.auth.lib.AdminUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {
    AdminUserDto fromEntity(AdminUser adminUser);
    AdminUser fromDto(AdminUserDto adminUserDto);
}
