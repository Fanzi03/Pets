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
import org.example.service.util.usuallycruds.PetUsualFunctionsService;
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

    @Qualifier("petUsualFunctionsService")
    PetUsualFunctionsService<Pet> petUsualFunctionsService;

    @Qualifier("petCreateServiceImpl")
    PetCreateService<Pet> petCreateServiceImpl;
    
    @Qualifier("petUpdateServiceImpl")
    PetUpdateService<Pet> petUpdateServiceImpl;

    public Page<PetDataTransferObject> gets(Pageable pageable) {
        return mapperService.mapPetsToDtoPage(petUsualFunctionsService.gets(pageable));
    }

    public PetDataTransferObject findById(Long petId){
        return mapperService.mapPetToDto(petUsualFunctionsService.findById(petId));
    }

    @Transactional
    public PetDataTransferObject add(PetDataTransferObject petDataTransferObject){
        PetDataTransferObject petUpdated = mapperService.mapPetToDto(
            petCreateServiceImpl.add(mapperService.mapToPet(petDataTransferObject))
        );
        return petUpdated;
    }

    @Transactional
    public void delete(Long id){
        Pet pet = petRepository.findById(id).orElseThrow(() -> 
            new NotFoundPetException("Pet with this id: " + id + " not found"
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
        petUsualFunctionsService.incrementAllPetsAge();        
    }

    @Transactional
    @Override
    public PetDataTransferObject addRandomPet() {
        return mapperService.mapPetToDto(petCreateServiceImpl.addRandomPet());
    }
}
