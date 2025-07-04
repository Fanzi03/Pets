package org.example.controller;

import javax.naming.AuthenticationException;

import org.example.dto.JwtAuthenticationDto;
import org.example.dto.RefreshTokenDto;
import org.example.dto.UserCredentialsDto;
import org.example.security.AuthService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    

    @Qualifier("authServiceImpl")
    AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationDto> signIn(@RequestBody UserCredentialsDto userCredentialsDto){
        try{
            JwtAuthenticationDto jwtAuthenticationDto = authService.signIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        }catch(AuthenticationException e){
            throw new RuntimeException("Authetication failed! " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception{
        return authService.refreshToken(refreshTokenDto);
    }

}
