package org.example.service;

import java.util.List;

import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.service.util.add.UserCreateService;
import org.example.service.util.updates.UserUpdateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService extends UserCreateService, UserUpdateService{
    public Page<UserDataTransferObject> getUsers(Pageable pageable);
    public List<PetDataTransferObject> getUserPets(Long id);
    public UserDataTransferObject findUserById(Long id);
    public UserDataTransferObject findUserByEmail(String email);
    public void deleteById(Long id);
}

