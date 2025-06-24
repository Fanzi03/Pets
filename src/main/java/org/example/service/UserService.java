package org.example.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.example.dto.JwtAuthenticationDto;
import org.example.dto.PetDataTransferObject;
import org.example.dto.RefreshTokenDto;
import org.example.dto.UserCredentialsDto;
import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.example.mapping.PetMapper;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.example.security.jwt.JwtService;
import org.example.service.util.add.UserCreateService;
import org.example.service.util.updates.UserUpdateService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    static String NOT_FOUND_MESSAGE = "User not found";
    JwtService jwtService;
    public static String ALREADY_EXISTS_MESSAGE = "- already exists";
    UserMapper userMapper;
    PetMapper petMapper;
    UserCreateService userCreateService;
    UserUpdateService userUpdateService;
    PasswordEncoder passwordEncoder;

    public List<UserDataTransferObject> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    public List<PetDataTransferObject> getUserPets(Long id) {
        return userRepository.findById(id).map(User::getPets)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE))
                .stream().map(petMapper::toDTO).collect(Collectors.toList());
    }

   // public List<UserDataTransferObjectWithPetList> getUserWithListPets(Long Id);

    @Transactional
    public Optional<UserDataTransferObject> findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    @Transactional
    public UserDataTransferObject findUserByEmail(String email){
        return userMapper.toDTO(userRepository.findByEmail(email)
            .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE + " with this email " + email)));
    }

    public UserDataTransferObject createUser(UserDataTransferObject userDataTransferObject) {
        return userCreateService.create(userDataTransferObject);
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));
        userRepository.delete(user);
    }

    public UserDataTransferObject update(UserDataTransferObject userDataTransferObject, Long id) {
        return userUpdateService.update(userDataTransferObject, id);
    }
    
    //security
    public JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException{
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(user.getEmail());
    }

    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception{
        String refreshToken = refreshTokenDto.getRefreshToken();
        if(refreshToken != null && jwtService.validateJwtToken(refreshToken)){
            User user = userRepository.findByEmail(jwtService.getEmailFromToken(refreshToken))
                .orElseThrow(() -> new NoSuchElementException("Not found email from token"));
            return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    private User findByCredentials (UserCredentialsDto userCredentialsDto)throws AuthenticationException{
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
