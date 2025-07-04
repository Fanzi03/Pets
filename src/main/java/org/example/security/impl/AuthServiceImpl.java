package org.example.security.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.example.dto.JwtAuthenticationDto;
import org.example.dto.RefreshTokenDto;
import org.example.dto.UserCredentialsDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.AuthService;
import org.example.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service("authServiceImpl")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    JwtService jwtService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(user.getEmail());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if(refreshToken != null && jwtService.validateJwtToken(refreshToken)){
            User user = userRepository.findByEmail(jwtService.getEmailFromToken(refreshToken))
                .orElseThrow(() -> new NoSuchElementException("Not found email from token"));
            return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    @Override
    public User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByEmail(userCredentialsDto.getEmail());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())){
                return user;
            }
        }
        throw new AuthenticationException("Email or password is wrong");
    }

}
