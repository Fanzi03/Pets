package org.example.dto.mapping;

import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PetMapper.class)
public interface UserMapper {
    UserDataTransferObject toDTO(User user);
}
