package org.example.dto.mapping;

import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import java.time.Year;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class, imports = { java.time.Year.class })
public interface PetMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "fullName", source = "user.fullName")
    PetDataTransferObject toDTO(Pet pet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", expression = "java(mapUserFromId(petDataTransferObject.getUserId()))")
    @Mapping(target = "birthYear", expression = "java(Year.now().getValue() - petDataTransferObject.getAge())")
    @Mapping(target = "createdAt", ignore = true)
    Pet toEntity(PetDataTransferObject petDataTransferObject);

    default User mapUserFromId(Long userId) {
        if(userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }
}
