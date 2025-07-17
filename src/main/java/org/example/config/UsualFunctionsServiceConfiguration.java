package org.example.config;

import org.example.entity.Pet;
import org.example.repository.PetRepository;
import org.example.service.util.usuallycruds.PetUsualFunctionsService;
import org.example.service.util.usuallycruds.impl.PetUsualFunctionsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class UsualFunctionsServiceConfiguration {

    @Bean("petUsualFunctionsService")
    @Primary
    public PetUsualFunctionsService<Pet> petUsualFunctionsService(
        PetRepository petRepository
    ){
        return new PetUsualFunctionsServiceImpl(petRepository);
    }
    
}
