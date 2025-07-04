package org.example.service.util.add.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.example.exception.custom.add.InvalidAddUserException;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.util.add.UserCreateService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCreateServiceImpl implements UserCreateService {
    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder; 
    
    @Transactional
    public UserDataTransferObject createUser(UserDataTransferObject userDataTransferObject) {
        if(userRepository.existsByEmail(userDataTransferObject.getEmail())) {
            throw new InvalidAddUserException("Email: " +
                    userDataTransferObject.getEmail() + " already exist");
        }

        if (userRepository.existsByUserName(userDataTransferObject.getUserName())) {
            throw new InvalidAddUserException("Username: " + userDataTransferObject.getUserName() +
                    " already exist");
        }


        User userEntity = userMapper.toEntity(userDataTransferObject);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userMapper.toDTO(userRepository.save(userEntity));
    }
}
