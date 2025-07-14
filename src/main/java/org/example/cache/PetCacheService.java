package org.example.cache;

import org.example.dto.PetDataTransferObject;
import org.example.service.PetService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetCacheService implements PetService, CacheCheckService{

    @Qualifier("petServiceImpl")
    PetService petServiceImpl;

    @Getter
    CacheService cacheService;

    @Override
    public PetDataTransferObject add(PetDataTransferObject petDataTransferObject) {
        PetDataTransferObject pet = petServiceImpl.add(petDataTransferObject);
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPagePattern());
        return pet;
    }

    @Override
    public PetDataTransferObject addRandomPet() {
        PetDataTransferObject pet = petServiceImpl.addRandomPet();
        cacheService.evictedByPattern(CacheKeyGenerator.firstPagesPattern());        
        return pet;
    }

    @Override
    public PetDataTransferObject update(Long id, PetDataTransferObject updatedPetDataTransferObject) {
        PetDataTransferObject updatedPet = petServiceImpl.update(id, updatedPetDataTransferObject);
        String key = CacheKeyGenerator.petKey(id);
        cacheService.put(key, updatedPet, 3600);
        if(isOwnerNameChanged(updatedPet.getOwnerName(), updatedPetDataTransferObject.getOwnerName()))
            cacheService.evict(CacheKeyGenerator.petUserKey(id));
        return updatedPet;
        // Pet doesn't have important fields except ownerName
    }

    @Override
    public Page<PetDataTransferObject> getPets(Pageable pageable) {
        return getFromCacheOrCompute(CacheKeyGenerator.petsPageKey(pageable.getPageNumber(), pageable.getPageSize()),
             Page.class, () -> petServiceImpl.getPets(pageable), 3600);
    }

    @Override
    public PetDataTransferObject findById(Long petId) {
        return getFromCacheOrCompute(CacheKeyGenerator.petKey(petId), PetDataTransferObject.class, 
            () -> petServiceImpl.findById(petId), 3600);
    }

    @Override
    public void delete(Long petId) {
        petServiceImpl.delete(petId);
        cacheService.evict(CacheKeyGenerator.petKey(petId));
        cacheService.evict(CacheKeyGenerator.petUserKey(petId));
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPagePattern());
    }

    private boolean isOwnerNameChanged(String originalOwner, String updatedOwner){
        if(originalOwner == null && updatedOwner == null)
            return false;

        if(originalOwner == null || updatedOwner == null)
            return true;
        
        if(updatedOwner.isEmpty())
            return false;

        return !originalOwner.equals(updatedOwner);
    }

    @Override
    public void incrementAllPetsAge() {
        petServiceImpl.incrementAllPetsAge();
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPattern());
        cacheService.evictedByPattern(CacheKeyGenerator.allPetsPagePattern());
    }
}
