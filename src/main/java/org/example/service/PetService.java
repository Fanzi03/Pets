package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Pet;
import org.example.service.util.PetUpdate;
import org.example.repository.PetRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> findById(Long petId){
        return petRepository.findById(petId);
    }

    public Pet add(Pet pet){
        pet.setCreatedAt(LocalDate.now());
        return petRepository.save(pet);
    }

    public void delete(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("pet with id: " + petId + " not found"));
        petRepository.delete(pet);
    }

    public Optional<Pet> update(Long id, Pet updatedPet){
        return PetUpdate.update(id, updatedPet, petRepository);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void incrementAllPetsAge(){
        List<Pet> pets = petRepository.findAll();
        for(Pet pet : pets){
            pet.setAge(pet.getAge()+1);
        }
        petRepository.saveAll(pets);
    }
}
