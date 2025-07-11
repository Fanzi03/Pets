package org.example.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.exception.custom.NotFoundPetException;
import org.example.mapping.PetMapper;
import org.example.repository.PetRepository;
import org.example.service.PetService;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.updates.PetUpdateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal =  true)
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    PetRepository petRepository;
    PetMapper petMapper;

    @Qualifier("petCreateServiceImpl")
    PetCreateService petCreateServiceImpl;
    
    @Qualifier("petUpdateServiceImpl")
    PetUpdateService petUpdateServiceImpl;

    public Page<PetDataTransferObject> getPets(Pageable pageable) {
        Page<Pet> pets = petRepository.findAll(pageable);
        if(!pets.hasContent()) throw new NotFoundPetException("Pets not found");

        Page<PetDataTransferObject> petDtos = pets.map(petMapper::toDTO);
        return petDtos;
    }

    public PetDataTransferObject findById(Long petId){
        return petMapper.toDTO(petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with id: " + petId + " not found") 
        ));
    }

    public PetDataTransferObject findByIdWithCach(Long id){
        PetDataTransferObject pet = findById(id);
        return pet;
    }

    public PetDataTransferObject add(PetDataTransferObject petDataTransferObject){
        PetDataTransferObject pet = petCreateServiceImpl.add(petDataTransferObject);
        return pet;
    }

    public void delete(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with this id: " + petId + " not found"
        ));
        petRepository.delete(pet);
    }

    public PetDataTransferObject update(Long id, PetDataTransferObject updatedPetDataTransferObject){
        PetDataTransferObject updatedPet = petUpdateServiceImpl.update(id, updatedPetDataTransferObject);
        return updatedPet;
        // Pet doesn't have important fields except ownerName
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void incrementAllPetsAge(){
        petRepository.incrementAllAge();
    }
}
