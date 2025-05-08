package org.example.dto.mapping;

import org.example.dto.PetDTO;
import org.example.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {
    @Mapping(target = "owner", expression = "java(pet.getUser().getFullName()")
    PetDTO toDTO(Pet pet);
}
