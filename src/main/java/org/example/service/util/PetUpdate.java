package org.example.service.util;


import org.example.dto.PetDataTransferObject;
import org.example.dto.mapping.PetMapper;
import org.example.entity.User;
import org.example.exception.InvalidPetUpdateException;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;


public final class PetUpdate {

    private PetUpdate(){}

    public static Optional<PetDataTransferObject> update(
            Long id,
            PetDataTransferObject updatedPetDTO,
            PetRepository petRepository,
            UserRepository userRepository,
            PetMapper petMapper
    ) {
        return petRepository.findById(id).map(
                existPet -> {

                    if(!existPet.getGender().equals(updatedPetDTO.getGender())){
                        throw new InvalidPetUpdateException("Gender cannot be changed!");
                    }

                    if(!existPet.getType().equals(updatedPetDTO.getType())){
                        throw new InvalidPetUpdateException("Type cannot be changed!");
                    }

                    if(!existPet.getName().equals(updatedPetDTO.getName())){
                        existPet.setName(updatedPetDTO.getName());
                    }

                    if(existPet.getAge() != updatedPetDTO.getAge()){
                        existPet.setAge(updatedPetDTO.getAge());
                    }

                    String ownerName = updatedPetDTO.getOwnerName();

                    if(ownerName == null){
                        existPet.setUser(null);
                    }
                    else{
                        if(existPet.getUser() == null || existPet.getUser().getUserName().equals(ownerName)) {
                        User user = userRepository.findByUserName(ownerName).orElseThrow(
                                () -> new NoSuchElementException("User with id:" + ownerName + " not found!"));
                          existPet.setUser(user);
                        }
                    }

                    return petMapper.toDTO(petRepository.save(existPet));
                }
        );
    }

}
