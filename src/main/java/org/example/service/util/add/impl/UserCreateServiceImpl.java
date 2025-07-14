package org.example.service.util.add.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.entity.User;
import org.example.exception.custom.add.InvalidAddUserException;
import org.example.repository.UserRepository;
import org.example.service.util.add.UserCreateService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCreateServiceImpl implements UserCreateService<User> {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder; 
    
    @Transactional
    public User createUser(User user) {
        validImmutableFields(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private void validImmutableFields(User user){
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new InvalidAddUserException("Email: " +
                    user.getEmail() + " already exist");
        }

        if (userRepository.existsByUserName(user.getUserName())) {
            throw new InvalidAddUserException("Username: " + user.getUserName() +
                    " already exist");
        }
    }
}
