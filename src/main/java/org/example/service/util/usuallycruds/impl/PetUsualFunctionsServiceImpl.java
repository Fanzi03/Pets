package org.example.service.util.usuallycruds.impl;

import org.example.entity.Pet;
import org.example.exception.custom.NotFoundPetException;
import org.example.repository.PetRepository;
import org.example.service.util.usuallycruds.PetUsualFunctionsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetUsualFunctionsServiceImpl implements PetUsualFunctionsService<Pet>{

    PetRepository petRepository;
    @Override
    public Page<Pet> gets(Pageable page) {
        Page<Pet> pets = petRepository.findAll(page);
        if(!pets.hasContent()) throw new NotFoundPetException("Pets not found");
        return pets;
    }

    @Override
    public Pet findById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> 
            new NotFoundPetException("Pet with id: " + id + " not found") 
        );
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void incrementAllPetsAge(){
        petRepository.incrementAllAge();
    }
    
}
