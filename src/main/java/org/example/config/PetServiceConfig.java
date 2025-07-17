package org.example.config;

import org.example.cache.CacheService;
import org.example.cache.PetCacheService;
import org.example.entity.Pet;
import org.example.mapping.MapperService;
import org.example.repository.PetRepository;
import org.example.service.PetService;
import org.example.service.impl.PetServiceImpl;
import org.example.service.util.UserResolver;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.add.impl.PetCreateServiceImpl;
import org.example.service.util.updates.PetUpdateService;
import org.example.service.util.updates.impl.PetUpdateServiceImpl;
import org.example.service.util.usuallycruds.PetUsualFunctionsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
        MapperService mapperService,
        @Qualifier("petUsualFunctionsService") PetUsualFunctionsService<Pet> usualFunctionsService,
        @Qualifier("petCreateServiceImpl") PetCreateService<Pet> petCreateService,
        @Qualifier("petUpdateServiceImpl") PetUpdateService<Pet> petUpdateService
    ){
        return new PetServiceImpl(
            petRepository, mapperService, usualFunctionsService, petCreateService, petUpdateService
        );
    }

    @Bean("petCreateServiceImpl")
    @Primary
    public PetCreateService<Pet> petCreateService(
        PetRepository petRepository, 
        UserResolver userResolver,
        @Value("${VALIDATION_ANIMAL_TYPES}") String[] animalTypes,
        @Value("${VALIDATION_ANIMAL_NAMES}") String[] animalNames
    ){
        return new PetCreateServiceImpl(petRepository, userResolver, animalTypes, animalNames);
    }

    @Bean("petUpdateServiceImpl")
    @Primary
    public PetUpdateService<Pet> petUpdateService(
        PetRepository petRepository,
        UserResolver userResolver
    ){
        return new PetUpdateServiceImpl(petRepository, userResolver);
    }
}
