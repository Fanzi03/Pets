package org.example.service.util.updates.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

import org.example.entity.Pet;
import org.example.entity.User;
import org.example.exception.custom.NotFoundPetException;
import org.example.exception.custom.update.InvalidPetUpdateException;
import org.example.repository.PetRepository;
import org.example.service.util.UserResolver;
import org.example.service.util.updates.PetUpdateService;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetUpdateServiceImpl implements PetUpdateService<Pet> {
    PetRepository petRepository;
    UserResolver userResolver;

    public Pet update(
            Long id,
            Pet updatedPet) {
        return petRepository.findById(id).map(
                existPet -> {
                    validImmutableFields(existPet, updatedPet);
                    updateMutableFields(existPet, updatedPet);
                    return petRepository.save(existPet);
                }).orElseThrow(() -> new NotFoundPetException("Pet not found"));
    }

    private void validImmutableFields(Pet existPet, Pet updatedPet) {
        if (!existPet.getGender().equals(updatedPet.getGender())) {
            throw new InvalidPetUpdateException("Gender cannot be changed!");
        }

        if (!existPet.getType().equals(updatedPet.getType())) {
            throw new InvalidPetUpdateException("Type cannot be changed!");
        }
    }

    private void updateMutableFields(Pet existPet, Pet updatedPet) {
        if (!existPet.getName().equals(updatedPet.getName())) {
            existPet.setName(updatedPet.getName());
        }

        if (existPet.getAge() != updatedPet.getAge()) {
            existPet.setAge(updatedPet.getAge());
        }

        String newOwnerName = updatedPet.getUser() != null ? updatedPet.getUser().getUserName() : null;
        String existOwnerName = existPet.getUser() != null ? existPet.getUser().getUserName() : null;

        if (!Objects.equals(newOwnerName, existOwnerName)) {
            User resolvedUser = userResolver.resolveUser(newOwnerName);
            existPet.setUser(resolvedUser);
        }
    }
}
