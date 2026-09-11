package com.backendsoft.foodmenu.auth;

import com.backendsoft.foodmenu.auth.lib.AdminUserDto;
import com.backendsoft.foodmenu.auth.lib.AuthRequest;
import com.backendsoft.foodmenu.utils.EntityNotFoundException;
import com.backendsoft.foodmenu.auth.lib.SecurityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping({"api/"})
public class SecurityController {
   private final SecurityService securityService;

    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("securities/auth/token")
    public AdminUserDto login(@RequestBody AuthRequest authRequestDto, HttpServletResponse response) throws EntityNotFoundException, IOException {
      return this.securityService.authToken(authRequestDto, response);
    }

//   @PutMapping({"securities/newpassword"})
//   public StaffDto updatePassword(@RequestBody CredentialDto credentialDto) throws InvalidEntityException, EntityNotFoundException {
//      return this.securityService.updatePassword(credentialDto);
//   }

//    @PostMapping("securities/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        securityService.refreshToken(request, response);
    }

    @PostMapping("securities/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) throws EntityNotFoundException {
        String newAccessToken = securityService.refresh(request);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + newAccessToken)
                .header("Access-Control-Expose-Headers", "Authorization")
                .build();
    }

}
