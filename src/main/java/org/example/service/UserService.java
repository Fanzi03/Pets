package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.dto.mapping.PetMapper;
import org.example.dto.mapping.UserMapper;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final String NOT_FOUND_MESSAGE = "User not found";
    private final UserMapper userMapper;
    private final PetMapper petMapper;

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

    public UserDataTransferObject createUser(User user) {
        return userMapper.toDTO(userRepository.save(user));
    }

    public void deleteById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));
        userRepository.delete(user);
    }

    public Optional<UserDataTransferObject> update(User user, Long id) {
        return userRepository.findById(id).map(
                exist ->
        {
            exist.setFullName(user.getFullName());
            exist.setPassword(user.getPassword());
            exist.setAge(user.getAge());
            exist.setEmail(user.getEmail());
            exist.setPets(user.getPets());
            return userMapper.toDTO(userRepository.save(exist));
        });
    }
}
