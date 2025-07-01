package org.example.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.naming.AuthenticationException;

import org.example.cache.CacheKeyGenerator;
import org.example.cache.CacheService;
import org.example.dto.JwtAuthenticationDto;
import org.example.dto.PetDataTransferObject;
import org.example.dto.RefreshTokenDto;
import org.example.dto.UserCredentialsDto;
import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.example.exception.custom.NotFoundUserException;
import org.example.mapping.PetMapper;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.example.security.jwt.JwtService;
import org.example.service.util.add.UserCreateService;
import org.example.service.util.updates.UserUpdateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    JwtService jwtService;
    UserMapper userMapper;
    PetMapper petMapper;
    UserCreateService userCreateService;
    UserUpdateService userUpdateService;
    PasswordEncoder passwordEncoder;
    CacheService cacheService;

    @Transactional(readOnly = true)
    public Page<UserDataTransferObject> getUsers(Pageable pageable) {
        String cacheKey = CacheKeyGenerator.usersPageKey(pageable.getPageNumber(), pageable.getPageSize());
        Page<UserDataTransferObject> cachedUsers = 
            (Page<UserDataTransferObject>) cacheService.get(cacheKey, Page.class);
        if(cachedUsers != null) return cachedUsers;
        Page<User> users = userRepository.findAll(pageable);
        if(!users.hasContent()){throw new NotFoundUserException("Users not found");};

        Page<UserDataTransferObject> userDtos = users.map(userMapper::toDTO);
        cacheService.put(cacheKey, userDtos, 2000);
        return userDtos;
    }

    public List<PetDataTransferObject> getUserPets(Long id) {
        String cacheKey = CacheKeyGenerator.userPetsKey(id);
        List<PetDataTransferObject> cachedUserPets = 
            (List<PetDataTransferObject>) cacheService.get(cacheKey, List.class);
        if(cachedUserPets != null) return cachedUserPets;
        List<PetDataTransferObject> pets = userRepository.findById(id).map(User::getPets)
                .orElseThrow(() -> new NotFoundUserException("User not found"))
                .stream().map(petMapper::toDTO).collect(Collectors.toList());
        cacheService.put(cacheKey, pets, 3600);
        return pets;
    }

    @Transactional
    public UserDataTransferObject findUserById(Long id) {
        return userMapper.toDTO(userRepository.findById(id).orElseThrow(() ->
            new NotFoundUserException("User with this id not found")
        ));
    }

    public UserDataTransferObject findUserByIdWithCash(Long id){
        String cacheKey = CacheKeyGenerator.userKey(id);
        UserDataTransferObject cachedUser = cacheService.get(cacheKey, UserDataTransferObject.class);
        if(cachedUser != null) return cachedUser;

        UserDataTransferObject user = findUserById(id);
        cacheService.put(cacheKey, user, 3600);
        return user;
    }

    @Transactional
    public UserDataTransferObject findUserByEmail(String email){
        return userMapper.toDTO(userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundUserException("User not found with this email " + email)));
    }

   public UserDataTransferObject findUserByEmailWithCache(String email){
        String cacheKey = CacheKeyGenerator.userEmailKey(email);
        UserDataTransferObject cachedUser = cacheService.get(cacheKey, UserDataTransferObject.class);
        if(cachedUser != null) return cachedUser;

        UserDataTransferObject user = findUserByEmail(email);
        cacheService.put(cacheKey, user, 3600);
        return user;
    }

    public UserDataTransferObject createUser(UserDataTransferObject userDataTransferObject) {
        UserDataTransferObject createdUser = userCreateService.create(userDataTransferObject);
        cacheService.evictedByPattern(CacheKeyGenerator.allUsersPagePattern());
        return createdUser;
    }

    public void deleteById(Long id) {
        if(!userRepository.existsById(id)) throw new NotFoundUserException("User not found with id " + id);
        userRepository.deleteById(id);
        String cacheKey = CacheKeyGenerator.userKey(id);
        cacheService.evict(cacheKey);
        cacheService.evict(CacheKeyGenerator.userPetsKey(id));
        cacheService.evictedByPattern(CacheKeyGenerator.allUserEmailPattern());
        cacheService.evictedByPattern(CacheKeyGenerator.allUsersPattern());
        cacheService.evictedByPattern(CacheKeyGenerator.allUsersPagePattern());
    }

    public UserDataTransferObject update(UserDataTransferObject userDataTransferObject, Long id) {
        UserDataTransferObject updatedUser = userUpdateService.update(userDataTransferObject, id);
        String cacheKey = CacheKeyGenerator.userKey(id);
        cacheService.put(cacheKey, updatedUser, 3600);
        if((!userDataTransferObject.getEmail().equals(updatedUser.getEmail())) 
            && (updatedUser.getEmail() != null && (!updatedUser.getEmail().isEmpty())))
                cacheService.evictedByPattern(CacheKeyGenerator.allUserEmailPattern());
        return updatedUser;
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
