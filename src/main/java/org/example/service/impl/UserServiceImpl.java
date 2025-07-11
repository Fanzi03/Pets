package org.example.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.example.exception.custom.NotFoundUserException;
import org.example.mapping.PetMapper;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.service.util.add.UserCreateService;
import org.example.service.util.updates.UserUpdateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    UserMapper userMapper;
    PetMapper petMapper;

    @Qualifier("userCreateServiceImpl")
    UserCreateService userCreateService;
    
    @Qualifier("userUpdateServiceImpl")
    UserUpdateService userUpdateService;

    @Transactional(readOnly = true)
    public Page<UserDataTransferObject> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        if(!users.hasContent()){throw new NotFoundUserException("Users not found");};

        Page<UserDataTransferObject> userDtos = users.map(userMapper::toDTO);
        return userDtos;
    }

    public List<PetDataTransferObject> getUserPets(Long id) {
        List<PetDataTransferObject> pets = userRepository.findById(id).map(User::getPets)
                .orElseThrow(() -> new NotFoundUserException("User not found"))
                .stream().map(petMapper::toDTO).collect(Collectors.toList());
        return pets;
    }

    @Transactional
    public UserDataTransferObject findUserById(Long id) {
        return userMapper.toDTO(userRepository.findById(id).orElseThrow(() ->
            new NotFoundUserException("User with this id not found")
        ));
    }

    @Transactional
    public UserDataTransferObject findUserByEmail(String email){
        return userMapper.toDTO(userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundUserException("User not found with this email " + email)));
    }

    public UserDataTransferObject createUser(UserDataTransferObject userDataTransferObject) {
        UserDataTransferObject createdUser = userCreateService.createUser(userDataTransferObject);
        return createdUser;
    }

    public void deleteById(Long id) {
        if(!userRepository.existsById(id)) throw new NotFoundUserException("User not found with id " + id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDataTransferObject update(UserDataTransferObject userDataTransferObject, Long id) {
        UserDataTransferObject updatedUser = userUpdateService.update(userDataTransferObject, id);
        return updatedUser;
    }
}
