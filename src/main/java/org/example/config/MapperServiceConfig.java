package org.example.config;

import org.example.mapping.MapperService;
import org.example.mapping.PetMapper;
import org.example.mapping.UserMapper;
import org.example.mapping.impl.MapperServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperServiceConfig {
    
    @Bean("mapperServiceImpl")
    public MapperService mapperServiceImpl(
        PetMapper petMapper,
        UserMapper userMapper
    ){
        return new MapperServiceImpl(petMapper,userMapper);
    }
}
