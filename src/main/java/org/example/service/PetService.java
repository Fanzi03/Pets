package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.exception.custom.NotFoundPetException;
import org.example.mapping.PetMapper;
import org.example.repository.PetRepository;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.updates.PetUpdateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final PetCreateService petCreateService;
    private final PetUpdateService petUpdateService;


    public Page<PetDataTransferObject> getPets(Pageable pageable) {
        Page<Pet> pets = petRepository.findAll(pageable);
        if(!pets.hasContent()) throw new NotFoundPetException("Pets not found");

        return pets.map(petMapper::toDTO);
    }

    public PetDataTransferObject findById(Long petId){
        return petMapper.toDTO(petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with id: " + petId + " not found") 
        ));
    }

    public PetDataTransferObject add(PetDataTransferObject petDataTransferObject){
        return petCreateService.add(petDataTransferObject);
    }

    public void delete(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with this id: " + petId + " not found"
        ));
        petRepository.delete(pet);
    }

    public PetDataTransferObject update(Long id, PetDataTransferObject updatedPetDataTransferObject){
        return petUpdateService.update(id, updatedPetDataTransferObject);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void incrementAllPetsAge(){
        petRepository.incrementAllAge();
    }
}
