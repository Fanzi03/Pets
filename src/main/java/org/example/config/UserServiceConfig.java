package org.example.config;

import org.example.cache.CacheService;
import org.example.cache.UserCacheService;
import org.example.mapping.PetMapper;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.example.service.util.add.UserCreateService;
import org.example.service.util.add.impl.UserCreateServiceImpl;
import org.example.service.util.updates.UserUpdateService;
import org.example.service.util.updates.impl.UserUpdateServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserServiceConfig {
    
    @Bean("userCacheService")
    @Primary
    public UserService userCacheService(
        @Qualifier("userServiceImpl") UserService userService,
        CacheService cacheService
    ){
        return new UserCacheService(userService, cacheService);
    }

    @Bean("userServiceImpl")
    public UserService userServiceImpl(
        UserRepository userRepository,
        UserMapper userMapper, 
        PetMapper petMapper,
        @Qualifier("userCreateServiceImpl") UserCreateService userCreateService,
        @Qualifier("userUpdateServiceImpl") UserUpdateService userUpdateService
    ){
        return new UserServiceImpl(userRepository, userMapper, petMapper, userCreateService, userUpdateService);
    }

    @Bean("userCreateServiceImpl")
    @Primary
    public UserCreateService userCreateServiceImpl(
        UserMapper userMapper,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
         return new UserCreateServiceImpl(userMapper, userRepository, passwordEncoder);
    }

    @Bean("userUpdateServiceImpl")
    @Primary
    public UserUpdateService userUpdateServiceImpl(
        UserRepository userRepository,
        UserMapper userMapper
    ){
        return new UserUpdateServiceImpl(userRepository, userMapper);
    }
}
