package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.example.cache.CacheKeyGenerator;
import org.example.cache.CacheService;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal =  true)
@RequiredArgsConstructor
public class PetService {
    PetRepository petRepository;
    PetMapper petMapper;
    PetCreateService petCreateService;
    PetUpdateService petUpdateService;
    CacheService cacheService;

    public Page<PetDataTransferObject> getPets(Pageable pageable) {
        String cacheKey = CacheKeyGenerator.petsPageKey(pageable.getPageNumber(), pageable.getPageSize());
        Page<PetDataTransferObject> cachedPet = 
            (Page<PetDataTransferObject>) cacheService.get(cacheKey, Page.class);
        if(cachedPet != null) return cachedPet;
        Page<Pet> pets = petRepository.findAll(pageable);
        if(!pets.hasContent()) throw new NotFoundPetException("Pets not found");

        Page<PetDataTransferObject> petDtos = pets.map(petMapper::toDTO);
        cacheService.put(cacheKey, petDtos, 3600);
        return petDtos;
    }

    public PetDataTransferObject findById(Long petId){
        return petMapper.toDTO(petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with id: " + petId + " not found") 
        ));
    }

    public PetDataTransferObject findByIdWithCach(Long id){
        String cacheKey = CacheKeyGenerator.petKey(id);
        PetDataTransferObject cachedPet = cacheService.get(cacheKey, PetDataTransferObject.class);
        if(cachedPet != null) return cachedPet;

        PetDataTransferObject pet = findById(id);
        cacheService.put(cacheKey, pet, 3600);
        return pet;
    }

    public PetDataTransferObject add(PetDataTransferObject petDataTransferObject){
        PetDataTransferObject pet = petCreateService.add(petDataTransferObject);
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPagePattern());
        return pet;
    }

    public void delete(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> 
            new NotFoundPetException("Pet with this id: " + petId + " not found"
        ));
        petRepository.delete(pet);
        cacheService.evict(CacheKeyGenerator.petKey(petId));
        cacheService.evict(CacheKeyGenerator.petUserKey(petId));
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPagePattern());
    }

    public PetDataTransferObject update(Long id, PetDataTransferObject updatedPetDataTransferObject){
        PetDataTransferObject updatedPet = petUpdateService.update(id, updatedPetDataTransferObject);
        String key = CacheKeyGenerator.petKey(id);
        cacheService.put(key, updatedPet, 3600);
        if((!(updatedPetDataTransferObject.getOwnerName().equals(updatedPet.getOwnerName())))
            && updatedPet.getOwnerName() != null && (!updatedPet.getOwnerName().isEmpty()))
                cacheService.evict(CacheKeyGenerator.petUserKey(id));
        return updatedPet;
        // Pet doesn't have important fields except ownerName
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void incrementAllPetsAge(){
        petRepository.incrementAllAge();
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPattern());
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPagePattern());
    }
}
