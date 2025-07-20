package org.example.config;

import org.example.repository.PetRepository;
import org.example.service.util.delete.PetDeleteService;
import org.example.service.util.delete.impl.PetDeleteServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteServiceConfig {

    @Bean("petDeleteServiceImpl")
    public PetDeleteService petDeleteServiceImpl(
        PetRepository petRepository
    ){
        return new PetDeleteServiceImpl(petRepository);
    }
    
}
