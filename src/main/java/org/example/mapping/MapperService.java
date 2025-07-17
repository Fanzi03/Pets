package org.example.mapping;

import java.util.List;
import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.entity.Pet;
import org.example.entity.User;
import org.example.mapping.util.MapperServiceHelper;
import org.springframework.data.domain.Page;

public interface MapperService extends MapperServiceHelper{

    List<PetDataTransferObject> mapPetsToDto(List<Pet> pets);

    List<Pet> mapToPets(List<PetDataTransferObject> pets);

    Pet mapToPet(PetDataTransferObject pet);

    PetDataTransferObject mapPetToDto(Pet pet);

    List<UserDataTransferObject> mapUsersToDto(List<User> users);

    List<User> mapToUsers(List<UserDataTransferObject> users);

    User mapToUser(UserDataTransferObject user);

    UserDataTransferObject mapUserToDto(User user);

    Page<Pet> mapToPetsPage(Page<PetDataTransferObject> pets);

    Page<PetDataTransferObject> mapPetsToDtoPage(Page<Pet> pets);

    Page<User> mapToUsersPage(Page<UserDataTransferObject> users);

    Page<UserDataTransferObject> mapUsersToDtosPage(Page<User> users);
}
