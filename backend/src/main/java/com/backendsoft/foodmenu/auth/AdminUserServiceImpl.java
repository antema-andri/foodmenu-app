package com.backendsoft.foodmenu.auth;

import com.backendsoft.foodmenu.auth.dao.AdminUserRepository;
import com.backendsoft.foodmenu.auth.lib.AdminUser;
import com.backendsoft.foodmenu.auth.lib.AdminUserDto;
import com.backendsoft.foodmenu.auth.lib.AdminUserService;
import com.backendsoft.foodmenu.auth.lib.AdminUserValidator;
import com.backendsoft.foodmenu.auth.mapper.AdminUserMapper;
import com.backendsoft.foodmenu.utils.ErrorCodes;
import com.backendsoft.foodmenu.utils.InvalidEntityException;
import com.backendsoft.foodmenu.utils.RegNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    private final AdminUserRepository adminUserRepository;
    private final AdminUserMapper adminUserMapper;

    public AdminUserServiceImpl(AdminUserRepository adminUserRepository, AdminUserMapper adminUserMapper) {
        this.adminUserRepository = adminUserRepository;
        this.adminUserMapper = adminUserMapper;
    }

    @Override
    public AdminUserDto save(AdminUserDto adminUserDto) throws InvalidEntityException {
        List<String> errors = AdminUserValidator.validate(adminUserDto);
        String prefixId= "ADMUSER";
        int lastAdminUserCount =(int) adminUserRepository.count();

        if (!errors.isEmpty()) {
            log.error("AdminUser is not valid: {}", adminUserDto);
            throw new InvalidEntityException("The customer is not valid", ErrorCodes.ADMINUSER_NOT_VALID, errors);
        }

        AdminUser adminUser=adminUserMapper.fromDto(adminUserDto);
        adminUser.setId(RegNumberUtil.generate(prefixId, lastAdminUserCount +1));
        adminUser.setPassword(new BCryptPasswordEncoder().encode(adminUserDto.getPassword()));

        return adminUserMapper.fromEntity(adminUserRepository.save(adminUser));
    }
}
