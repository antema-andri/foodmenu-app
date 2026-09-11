package com.backendsoft.foodmenu.auth;

import com.backendsoft.foodmenu.auth.dao.AdminUserRepository;
import com.backendsoft.foodmenu.auth.lib.AdminUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AdminUserRepository adminUserRepository;

    public UserDetailsServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
      AdminUser adminUser = adminUserRepository.findByUsernameOrEmail(usernameOrEmail).orElseThrow(() ->
         new UsernameNotFoundException("INVALID USER")
      );
      Collection<GrantedAuthority> authorities = new ArrayList<>();
      return User.withUsername(adminUser.getUsername()).password(adminUser.getPassword()).authorities(authorities).build();
    }
}
