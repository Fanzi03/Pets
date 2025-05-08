package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetDTO;
import org.example.dto.mapping.PetMapper;
import org.example.entity.Pet;
import org.example.repository.PetRepository;
import org.example.repository.UserRepository;
import org.example.service.util.PetUpdate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;


    public List<PetDTO> getPets() {
        return petRepository.findAll().stream().map(petMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<PetDTO> findById(Long petId){
        return petRepository.findById(petId).map(petMapper::toDTO);
    }

    public PetDTO add(Pet pet){
        pet.setCreatedAt(LocalDate.now());
        return petMapper.toDTO(petRepository.save(pet));
    }

    public void delete(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NoSuchElementException("pet with id: " + petId + " not found"));
        petRepository.delete(pet);
    }

    public Optional<PetDTO> update(Long id, Pet updatedPet){
        return PetUpdate.update(id, updatedPet, petRepository, userRepository, petMapper);
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
