package org.example.security;

import javax.naming.AuthenticationException;

import org.example.dto.JwtAuthenticationDto;
import org.example.dto.RefreshTokenDto;
import org.example.dto.UserCredentialsDto;
import org.example.entity.User;

public interface AuthService {

   public JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;
   public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception;
   public User findByCredentials (UserCredentialsDto userCredentialsDto)throws AuthenticationException;
}
