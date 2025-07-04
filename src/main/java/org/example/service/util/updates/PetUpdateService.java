package org.example.service.util.updates;

import org.example.dto.PetDataTransferObject;

public interface PetUpdateService {
    public PetDataTransferObject update(Long id, PetDataTransferObject petDataTransferObject);
}
