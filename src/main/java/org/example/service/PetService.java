package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.mapping.PetMapper;
import org.example.repository.PetRepository;
import org.example.service.util.add.PetCreateService;
import org.example.service.util.updates.PetUpdateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final PetCreateService petCreateService;
    private final PetUpdateService petUpdateService;


    public List<PetDataTransferObject> getPets() {
        return petRepository.findAll().stream().map(petMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<PetDataTransferObject> findById(Long petId){
        return petRepository.findById(petId).map(petMapper::toDTO);
    }

    public PetDataTransferObject add(PetDataTransferObject petDataTransferObject){
        return petCreateService.add(petDataTransferObject);
    }

    public void delete(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("pet with id: " + petId + " not found"));
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
