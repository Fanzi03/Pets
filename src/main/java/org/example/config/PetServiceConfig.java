package org.example.config;

import org.example.cache.CacheService;
import org.example.cache.PetCacheService;
import org.example.mapping.PetMapper;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;
import org.example.service.PetService;
import org.example.service.impl.PetServiceImpl;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.add.impl.PetCreateServiceImpl;
import org.example.service.util.updates.PetUpdateService;
import org.example.service.util.updates.impl.PetUpdateServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class PetServiceConfig {
    
    @Bean("petCacheService")
    @Primary
    public PetService petCacheService(
        @Qualifier("petServiceImpl") PetService petService,
        CacheService cacheService
    ){
        return new PetCacheService(petService, cacheService);
    }

    @Bean("petServiceImpl")
    public PetService petServiceImpl(
        PetRepository petRepository,
        PetMapper petMapper,
        @Qualifier("petCreateServiceImpl") PetCreateService petCreateService,
        @Qualifier("petUpdateServiceImpl") PetUpdateService petUpdateService
    ){
        return new PetServiceImpl(petRepository, petMapper, petCreateService, petUpdateService);
    }

    @Bean("petCreateServiceImpl")
    @Primary
    public PetCreateService petCreateService(
        PetRepository petRepository, 
        PetMapper petMapper,
        UserRepository userRepository
    ){
        return new PetCreateServiceImpl(petRepository, petMapper, userRepository);
    }

    @Bean("petUpdateServiceImpl")
    @Primary
    public PetUpdateService petUpdateService(
        PetRepository petRepository,
        PetMapper petMapper,
        UserRepository userRepository
    ){
        return new PetUpdateServiceImpl(petRepository, petMapper, userRepository);
    }
}
