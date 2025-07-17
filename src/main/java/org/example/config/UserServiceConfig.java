package org.example.config;

import org.example.cache.CacheService;
import org.example.cache.UserCacheService;
import org.example.entity.User;
import org.example.mapping.MapperService;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.service.impl.UserServiceImpl;
import org.example.service.util.UserResolver;
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
        MapperService mapperService,
        @Qualifier("userCreateServiceImpl") UserCreateService<User> userCreateService,
        @Qualifier("userUpdateServiceImpl") UserUpdateService<User> userUpdateService
    ){
        return new UserServiceImpl(userRepository, mapperService, userCreateService, userUpdateService);
    }

    @Bean("userCreateServiceImpl")
    @Primary
    public UserCreateService<User> userCreateServiceImpl(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
         return new UserCreateServiceImpl(userRepository, passwordEncoder);
    }

    @Bean("userUpdateServiceImpl")
    @Primary
    public UserUpdateService<User> userUpdateServiceImpl(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder){
        return new UserUpdateServiceImpl (userRepository, passwordEncoder);
    }

    @Bean("userResolver")
    public UserResolver userResolver(UserRepository userRepository){
        return new UserResolver(userRepository);
    }
}
