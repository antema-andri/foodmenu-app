package com.backendsoft.foodmenu.auth.lib;

import com.backendsoft.foodmenu.utils.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface SecurityService {
//   AdminUserDto authenticate(AuthRequest authRequest, HttpServletResponse response) throws EntityNotFoundException;

   AdminUserDto authToken(AuthRequest authRequest, HttpServletResponse response) throws EntityNotFoundException, IOException;

//   StaffDto updatePassword(CredentialDto credentialDto) throws InvalidEntityException, EntityNotFoundException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    String refresh(HttpServletRequest request) throws EntityNotFoundException;
}
