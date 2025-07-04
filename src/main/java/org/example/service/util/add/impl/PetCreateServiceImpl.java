package org.example.service.util.add.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.entity.User;
import org.example.exception.custom.add.InvalidAddPetException;
import org.example.mapping.PetMapper;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;
import org.example.service.util.add.PetCreateService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetCreateServiceImpl implements PetCreateService {
    PetRepository petRepository;
    PetMapper petMapper;
    UserRepository userRepository;

    @Transactional
    public PetDataTransferObject add (PetDataTransferObject petDataTransferObject) {
        Pet petEntity = petMapper.toEntity(petDataTransferObject);

        String ownerName = petDataTransferObject.getOwnerName();

        if(ownerName != null && !ownerName.isBlank()){
            User user = userRepository.findByUserName(ownerName)
                    .orElseThrow(() -> new InvalidAddPetException("User with userName " + ownerName + " not found"));
            petEntity.setUser(user);
        }
        else petEntity.setUser(null); // without owner

        petEntity.setCreatedAt(LocalDate.now());
        return petMapper.toDTO(petRepository.save(petEntity));
    }
}


