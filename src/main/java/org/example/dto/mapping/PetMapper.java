package org.example.dto.mapping;

import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface PetMapper {
    @Mapping(target = "fullName", expression = "java(pet.getUser().getFullName())")
    PetDataTransferObject toDTO(Pet pet);
}
