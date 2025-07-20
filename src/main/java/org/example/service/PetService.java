package org.example.service;

import org.example.dto.PetDataTransferObject;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.delete.PetDeleteService;
import org.example.service.util.updates.PetUpdateService;
import org.example.service.util.usuallycruds.PetUsualFunctionsService;

public interface PetService extends 
        PetCreateService<PetDataTransferObject>, PetUpdateService<PetDataTransferObject>, 
        PetUsualFunctionsService<PetDataTransferObject>, PetDeleteService
{}
