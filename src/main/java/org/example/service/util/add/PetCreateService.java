package org.example.service.util.add;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.PetDataTransferObject;
import org.example.dto.mapping.PetMapper;
import org.example.entity.Pet;
import org.example.entity.User;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetCreateService {
    PetRepository petRepository;
    PetMapper petMapper;
    UserRepository userRepository;

    @Transactional
    public PetDataTransferObject add (PetDataTransferObject petDataTransferObject) {
        Pet petEntity = petMapper.toEntity(petDataTransferObject);

        String ownerName = petDataTransferObject.getOwnerName();
        System.out.println("User in Pet: " + ownerName); // не должен быть null

        if(ownerName != null && !ownerName.isBlank()){
            User user = userRepository.findByUserName(ownerName)
                    .orElseThrow(() -> new NoSuchElementException("User with userName " + ownerName + " not found"));
            System.out.println("User found: " + user.getFullName());
            petEntity.setUser(user);
            System.out.println("User in petEntity: " + petEntity.getUser());
        }
        else petEntity.setUser(null); // without owner

        petEntity.setCreatedAt(LocalDate.now());
        return petMapper.toDTO(petRepository.save(petEntity));
    }
}


