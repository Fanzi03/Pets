package org.example.mapping.impl;

import java.util.List;

import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.entity.Pet;
import org.example.entity.User;
import org.example.mapping.MapperService;
import org.example.mapping.PetMapper;
import org.example.mapping.UserMapper;
import org.springframework.data.domain.Page;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapperServiceImpl implements MapperService{

    PetMapper petMapper;
    UserMapper userMapper;

    public List<PetDataTransferObject> mapPetsToDto(List<Pet> pets){
        return mapList(pets, petMapper::toDTO);
    }

    public List<Pet> mapToPets(List<PetDataTransferObject> pets){
        return mapList(pets, petMapper::toEntity);
    }

    public Pet mapToPet(PetDataTransferObject pet){
        return map(pet, petMapper::toEntity);
    }

    public PetDataTransferObject mapPetToDto(Pet pet){
        return map(pet, petMapper::toDTO);
    }

    public List<UserDataTransferObject> mapUsersToDto(List<User> users){
        return mapList(users, userMapper::toDTO);
    }

    public List<User> mapToUsers(List<UserDataTransferObject> users){
        return mapList(users, userMapper::toEntity);
    }

    public User mapToUser(UserDataTransferObject user){
        return map(user, userMapper::toEntity);
    }

    public UserDataTransferObject mapUserToDto(User user){
        return map(user, userMapper::toDTO);
    }

    @Override
    public Page<Pet> mapToPetsPage(Page<PetDataTransferObject> pets) {
        return mapPage(pets, petMapper::toEntity);
    }

    @Override
    public Page<PetDataTransferObject> mapPetsToDtoPage(Page<Pet> pets) {
        return mapPage(pets, petMapper::toDTO);
    }

    @Override
    public Page<User> mapToUsersPage(Page<UserDataTransferObject> users) {
        return mapPage(users, userMapper::toEntity);
    }

    @Override
    public Page<UserDataTransferObject> mapUsersToDtosPage(Page<User> users) {
        return mapPage(users, userMapper::toDTO);
    }
}

