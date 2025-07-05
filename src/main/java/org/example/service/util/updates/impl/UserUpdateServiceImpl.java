package org.example.service.util.updates.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.UserDataTransferObject;
import org.example.exception.custom.update.InvalidUserUpdateException;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.util.updates.UserUpdateService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserUpdateServiceImpl implements UserUpdateService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    @Transactional
    public UserDataTransferObject update (UserDataTransferObject userDataTransferObject, Long id) {
        return userRepository.findById(id).map(
                existUser ->
                {
                    if(!existUser.getUserName().equals(userDataTransferObject.getUserName())) {
                        throw new InvalidUserUpdateException("UserName cannot be change");
                    }

                    if(!existUser.getEmail().equals(userDataTransferObject.getEmail())) {
                        existUser.setEmail(userDataTransferObject.getEmail());
                    }

                    if(!existUser.getPassword().equals(userDataTransferObject.getPassword())) {
                        existUser.setPassword(passwordEncoder.encode(userDataTransferObject.getPassword()));
                    }

                    if(existUser.getAge() != userDataTransferObject.getAge()) {
                        existUser.setAge(userDataTransferObject.getAge());
                    }
                    if(!existUser.getFullName().equals(userDataTransferObject.getFullName())) {
                        existUser.setFullName(userDataTransferObject.getFullName());
                    }

                    return userMapper.toDTO(userRepository.save(existUser));
                }).orElseThrow(() -> 
		    new InvalidUserUpdateException("User with id " + id + " not exist"));
    }
}
