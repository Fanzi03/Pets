package org.example.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.exception.custom.NotFoundPetException;
import org.example.mapping.MapperService;
import org.example.repository.PetRepository;
import org.example.service.PetService;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.updates.PetUpdateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal =  true)
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    PetRepository petRepository;
    MapperService mapperService;

    @Qualifier("petCreateServiceImpl")
    PetCreateService<Pet> petCreateServiceImpl;
    
    @Qualifier("petUpdateServiceImpl")
    PetUpdateService<Pet> petUpdateServiceImpl;

    public Page<PetDataTransferObject> getPets(Pageable pageable) {
        Page<Pet> pets = petRepository.findAll(pageable);
        if(!pets.hasContent()) throw new NotFoundPetException("Pets not found");

        return mapperService.mapPetsToDtoPage(pets);
    }

    public PetDataTransferObject findById(Long petId){
        return mapperService.mapPetToDto(petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with id: " + petId + " not found") 
        ));
    }

    @Transactional
    public PetDataTransferObject add(PetDataTransferObject petDataTransferObject){
        PetDataTransferObject petUpdated = mapperService.mapPetToDto(
            petCreateServiceImpl.add(mapperService.mapToPet(petDataTransferObject))
        );
        return petUpdated;
    }

    @Transactional
    public void delete(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with this id: " + petId + " not found"
        ));
        petRepository.delete(pet);
    }

    @Transactional
    public PetDataTransferObject update(Long id, PetDataTransferObject updatedPetDataTransferObject){ 
        return mapperService.mapPetToDto(
            petUpdateServiceImpl.update(id, mapperService.mapToPet(updatedPetDataTransferObject))
        );
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void incrementAllPetsAge(){
        petRepository.incrementAllAge();
    }

    @Transactional
    @Override
    public PetDataTransferObject addRandomPet() {
        return mapperService.mapPetToDto(petCreateServiceImpl.addRandomPet());
    }
}
