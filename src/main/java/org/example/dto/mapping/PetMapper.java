package org.example.dto.mapping;


import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = UserMapper.class, imports = { java.time.Year.class })
public interface PetMapper {

    @Mapping(target = "ownerName",
            expression = "java(pet.getUser() != null ? pet.getUser().getUserName() : null)")
    PetDataTransferObject toDTO(Pet pet);

    @Mapping(target = "birthYear", expression = "java(Year.now().getValue() - petDataTransferObject.getAge())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Pet toEntity(PetDataTransferObject petDataTransferObject);



}
