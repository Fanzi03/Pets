package org.example.mapping;

import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PetMapper.class)
public interface UserMapper {

    @Mapping(target = "password", expression = "java(generateRandomStars())")
    UserDataTransferObject toDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDataTransferObject userDataTransferObject);

    default String generateRandomStars(){
        int length = 6 + (int) (Math.random() * 5);
        return "*".repeat(length);
    }
}


