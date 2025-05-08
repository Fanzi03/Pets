package org.example.service.util;


import org.example.dto.PetDataTransferObject;
import org.example.dto.mapping.PetMapper;
import org.example.entity.Pet;
import org.example.entity.User;
import org.example.exception.InvalidPetUpdateException;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;


public final class PetUpdate {

    private PetUpdate(){}

    public static Optional<PetDataTransferObject> update(Long id, Pet updatedPet, PetRepository petRepository, UserRepository userRepository, PetMapper petMapper) {
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
                    if(updatedPet.getUser() != null && updatedPet.getUser().getId() != null && (existPet.getUser() == null || !existPet.getUser().getId().equals(updatedPet.getUser().getId()))){
                        User user = userRepository.findById(updatedPet.getUser().getId()).orElseThrow(
                                () -> new NoSuchElementException("User with id:" + id + " not found!"));
                        existPet.setUser(user);
                    }

                    return petMapper.toDTO(petRepository.save(existPet));
                }
        );
    }

}
