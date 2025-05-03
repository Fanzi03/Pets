package org.example.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.entity.Pet;
import org.example.exception.InvalidPetUpdateException;
import org.example.repository.PetRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
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
        return petRepository.save(pet);
    }

    public boolean delete(Long petId){
        if(petRepository.existsById(petId)){
            petRepository.deleteById(petId);
            return true;
        }
       return false;
    }

    public Optional<Pet> update(Long id, Pet updatedPet){
        return petRepository.findById(id).map(
                existPet -> {
                    if(!existPet.getGender().equals(updatedPet.getGender())){
                        throw new InvalidPetUpdateException("Gender cannot be changed!");
                    }

                    if(!existPet.getType().equals(updatedPet.getType())){
                        throw new InvalidPetUpdateException("Type cannot be changed!");
                    }
                    
                    existPet.setName(updatedPet.getName());
                    existPet.setAge(updatedPet.getAge());
                    return petRepository.save(existPet);
                }
        );
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void growPer(){
        for(Pet pet : petRepository.findAll()){
            pet.setAge(pet.getAge()+1);
        }
    }
}
