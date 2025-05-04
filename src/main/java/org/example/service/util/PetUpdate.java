package org.example.service.util;


import org.example.entity.Pet;
import org.example.exception.InvalidPetUpdateException;
import org.example.repository.PetRepository;

import java.util.Optional;


public final class PetUpdate {

    private PetUpdate(){}

    public static Optional<Pet> update(Long id, Pet updatedPet, PetRepository petRepository) {
        return petRepository.findById(id).map(
                existPet -> {

                    if(!existPet.getGender().equals(updatedPet.getGender())){
                        throw new InvalidPetUpdateException("Gender cannot be changed!");
                    }

                    if(!existPet.getType().equals(updatedPet.getType())){
                        throw new InvalidPetUpdateException("Type cannot be changed!");
                    }

                    if(!existPet.getName().equals(updatedPet.getName())){
                        existPet.setName(updatedPet.getName());
                    }

                    if(existPet.getAge() != updatedPet.getAge()){
                        existPet.setAge(updatedPet.getAge());
                    }

                    return petRepository.save(existPet);
                }
        );
    }

}
