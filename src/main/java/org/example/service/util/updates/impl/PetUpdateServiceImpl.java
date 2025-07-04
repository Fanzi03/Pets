package org.example.service.util.updates.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.PetDataTransferObject;
import org.example.entity.User;
import org.example.exception.custom.NotFoundPetException;
import org.example.exception.custom.NotFoundUserException;
import org.example.exception.custom.update.InvalidPetUpdateException;
import org.example.mapping.PetMapper;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;
import org.example.service.util.updates.PetUpdateService;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetUpdateServiceImpl implements PetUpdateService{
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
                                () -> new NotFoundUserException("User with userName:" + ownerName + " not found!"));
                          existPet.setUser(user);
                        }
                    }

                    return petMapper.toDTO(petRepository.save(existPet));
                }
        ).orElseThrow(() -> new NotFoundPetException("Pet not found")); 
    }

}
