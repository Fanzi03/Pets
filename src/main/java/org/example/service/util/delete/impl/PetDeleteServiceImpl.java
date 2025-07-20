package org.example.service.util.delete.impl;

import org.example.entity.Pet;
import org.example.exception.custom.NotFoundPetException;
import org.example.repository.PetRepository;
import org.example.service.util.delete.PetDeleteService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetDeleteServiceImpl implements PetDeleteService{

    PetRepository petRepository;

    @Override
    public void delete(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> 
            new NotFoundPetException("Pet with this id: " + id + " not found"
        ));
        petRepository.delete(pet);
    }
    
}
