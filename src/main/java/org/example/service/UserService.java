package org.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.dto.mapping.PetMapper;
import org.example.dto.mapping.UserMapper;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.util.add.UserCreateService;
import org.example.service.util.updates.UserUpdateService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    static String NOT_FOUND_MESSAGE = "User not found";
    public static String ALREADY_EXISTS_MESSAGE = "- already exists";
    UserMapper userMapper;
    PetMapper petMapper;
    UserCreateService userCreateService;
    UserUpdateService userUpdateService;

    public List<UserDataTransferObject> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    public List<PetDataTransferObject> getUserPets(Long id) {
        return userRepository.findById(id).map(User::getPets)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE))
                .stream().map(petMapper::toDTO).collect(Collectors.toList());
    }

    public Optional<UserDataTransferObject> findUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public UserDataTransferObject createUser(UserDataTransferObject userDataTransferObject) {
        return userCreateService.create(userDataTransferObject);
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));
        userRepository.delete(user);
    }

    public Optional<UserDataTransferObject> update(UserDataTransferObject userDataTransferObject, Long id) {
        return userUpdateService.update(userDataTransferObject, id);
    }
}
