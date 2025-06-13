package org.example.service.util.updates;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.PetDataTransferObject;
import org.example.dto.mapping.PetMapper;
import org.example.entity.User;
import org.example.exception.InvalidPetUpdateException;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetUpdateService {
    PetRepository petRepository;
    PetMapper petMapper;
    UserRepository userRepository;

    @Transactional
    public PetDataTransferObject update(
            Long id,
            PetDataTransferObject updatedPetDTO
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
                        if(existPet.getUser() == null || !existPet.getUser().getUserName().equals(ownerName)) {
                        User user = userRepository.findByUserName(ownerName).orElseThrow(
                                () -> new NoSuchElementException("User with userName:" + ownerName + " not found!"));
                          existPet.setUser(user);
                        }
                    }

                    return petMapper.toDTO(petRepository.save(existPet));
                }
        ).orElseThrow(() -> new InvalidPetUpdateException("No pets with id " + id)
	    );
    }

}
