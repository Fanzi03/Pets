package org.example.dto.mapping;

import org.example.dto.UserDataTransferObject;
import org.example.dto.util.UserDataTransferObjectWithPetList;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = PetMapper.class)
public interface UserMapper {

    UserDataTransferObject toDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDataTransferObject userDataTransferObject);

    UserDataTransferObjectWithPetList toDTOWithPetList(User user);
}


