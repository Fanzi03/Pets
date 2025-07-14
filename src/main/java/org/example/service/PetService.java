package org.example.service;

import org.example.dto.PetDataTransferObject;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.updates.PetUpdateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetService extends PetCreateService<PetDataTransferObject>, PetUpdateService<PetDataTransferObject>{
    public Page<PetDataTransferObject> getPets(Pageable pageable);
    public PetDataTransferObject findById(Long petId);
    public void delete(Long petId);
    public void incrementAllPetsAge();
}
