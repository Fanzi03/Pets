package org.example.service.util.add.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.example.entity.Pet;
import org.example.entity.User;
import org.example.enums.Gender;
import org.example.repository.PetRepository;
import org.example.service.util.UserResolver;
import org.example.service.util.add.PetCreateService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetCreateServiceImpl implements PetCreateService<Pet> {
    PetRepository petRepository;
    UserResolver userResolver;

    @NonFinal String[] animalTypes;
    @NonFinal String[] animalNames;

    @Transactional
    public Pet add (Pet pet) {
        String ownerName = pet.getUser() != null ? pet.getUser().getUserName() : null;
        User resolvedUser = userResolver.resolveUser(ownerName);

        pet.setUser(resolvedUser);
        pet.setCreatedAt(LocalDate.now());
        return petRepository.save(pet);
    }

    @Override
    @Transactional
    public Pet addRandomPet() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        int randomAge = threadLocalRandom.nextInt(20);
        int randomNumber = threadLocalRandom.nextInt(2);

        Gender gender = generateRandomGender(randomNumber);
        String randomName = animalNames[threadLocalRandom.nextInt(animalNames.length)];
        String randomType = animalTypes[threadLocalRandom.nextInt(animalTypes.length)];

        Pet randomPet = Pet.builder().name(randomName).age(randomAge).createdAt(LocalDate.now()).user(null)
            .birthYear(Year.now().getValue() - randomAge).type(randomType).gender(gender).build();

        return petRepository.save(randomPet);
    }

    private Gender generateRandomGender(int randomNumber){
        if(randomNumber == 1) return Gender.MAN;
        return Gender.WOMAN;
    }
}