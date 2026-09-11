package com.backendsoft.foodmenu.auth;

import com.backendsoft.foodmenu.auth.lib.JwtServiceImpl;
import com.backendsoft.foodmenu.auth.lib.SecurityService;
import com.backendsoft.foodmenu.auth.dao.AdminUserRepository;
import com.backendsoft.foodmenu.auth.lib.AdminUserDto;
import com.backendsoft.foodmenu.auth.lib.AuthRequest;
import com.backendsoft.foodmenu.auth.lib.AuthenticationResponse;
import com.backendsoft.foodmenu.auth.mapper.AdminUserMapper;
import com.backendsoft.foodmenu.utils.EntityNotFoundException;
import com.backendsoft.foodmenu.auth.lib.AdminUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final AdminUserRepository adminUserRepository;
    private final AuthenticationManager authenticationManager;
    private final AdminUserMapper adminUserMapper;

    private final JwtServiceImpl jwtService;

    public SecurityServiceImpl(AdminUserRepository adminUserRepository, AuthenticationManager authenticationManager, AdminUserMapper adminUserMapper, JwtServiceImpl jwtService) {
        this.adminUserRepository = adminUserRepository;
        this.authenticationManager = authenticationManager;
        this.adminUserMapper = adminUserMapper;
        this.jwtService = jwtService;
    }

    @Override
    public AdminUserDto authToken(AuthRequest authRequestDto, HttpServletResponse response) throws EntityNotFoundException, IOException {
        AdminUser adminUser = adminUserRepository
                .findByUsernameOrEmail(authRequestDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with login: " + authRequestDto.getUsername() + " does not exist"
                ));

        String accessToken;
        String refreshToken;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDto.getUsername(),
                            authRequestDto.getPassword()
                    )
            );

            UserDetails userDetails = User.withUsername(adminUser.getUsername())
                    .password(adminUser.getPassword())
                    .authorities("ROLE_ADMIN")
                    .build();

            accessToken = jwtService.generateToken(userDetails);
            refreshToken = jwtService.generateRefreshToken(userDetails);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("The password is incorrect!");
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Authentication error!") {};
        }

        // User DTO
        AdminUserDto adminUserDto = this.adminUserMapper.fromEntity(adminUser);
        adminUserDto.setPassword(null);

        // Access token (unchanged → frontend OK)
        response.addHeader("Authorization", "Bearer " + accessToken);

        // Refresh token (new → invisible to frontend)
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)   // inaccessible in JS
                .secure(true)     // set to false locally if HTTP
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());

        return adminUserDto;
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            AdminUser adminUser = adminUserRepository.findByUsernameOrEmail(userEmail)
                    .orElseThrow();
            UserDetails userDetails = User.withUsername(adminUser.getUsername())
                    .build();
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtService.generateToken(userDetails);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }

    @Override
    public String refresh(HttpServletRequest request) throws EntityNotFoundException {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new EntityNotFoundException("No cookies found");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        String username = jwtService.extractUsername(refreshToken);

        AdminUser adminUser = adminUserRepository
                .findByUsernameOrEmail(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with login: " + username + " does not exist"
                ));

        UserDetails userDetails = User.withUsername(username).build();

        if (refreshToken == null || !jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new EntityNotFoundException("Invalid token");
        }

        String newAccessToken = jwtService.generateToken(userDetails);

        return newAccessToken;
    }
}
