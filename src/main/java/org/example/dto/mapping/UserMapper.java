package org.example.dto.mapping;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = PetMapper.class)
public interface UserMapper {
    UserDTO toDTO(User user);
}
