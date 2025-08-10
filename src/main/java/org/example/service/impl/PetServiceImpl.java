package org.example.service.impl;

import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.kafka.PetKafkaProducer;
import org.example.mapping.MapperService;
import org.example.service.PetService;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.delete.PetDeleteService;
import org.example.service.util.updates.PetUpdateService;
import org.example.service.util.usuallycruds.PetUsualFunctionsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal =  true)
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    MapperService mapperService;
    PetDeleteService petDeleteService;
    PetUsualFunctionsService<Pet> petUsualFunctionsService;
    PetCreateService<Pet> petCreateServiceImpl;
    PetUpdateService<Pet> petUpdateServiceImpl;
    PetKafkaProducer petKafkaProducer;

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
        petDeleteService.delete(id);
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
        Pet petRandom = petCreateServiceImpl.addRandomPet();
//        petKafkaProducer.sendPetToKafka(petRandom);
        return mapperService.mapPetToDto(petRandom);
    }
}
