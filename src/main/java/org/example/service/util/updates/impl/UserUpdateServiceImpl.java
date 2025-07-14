package org.example.service.util.updates.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.entity.User;
import org.example.exception.custom.update.InvalidUserUpdateException;
import org.example.repository.UserRepository;
import org.example.service.util.updates.UserUpdateService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserUpdateServiceImpl implements UserUpdateService<User> {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
    public User update (User user, Long id) {
        return userRepository.findById(id).map(
                existUser ->
                {
                    validImmutableFields(existUser, user);

                    if(!existUser.getEmail().equals(user.getEmail())) {
                        existUser.setEmail(user.getEmail());
                    }

                    if(!existUser.getPassword().equals(user.getPassword())) {
                        existUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }

                    if(existUser.getAge() != user.getAge()) {
                        existUser.setAge(user.getAge());
                    }
                    if(!existUser.getFullName().equals(user.getFullName())) {
                        existUser.setFullName(user.getFullName());
                    }

                    return userRepository.save(existUser);
                }).orElseThrow(() -> 
		    new InvalidUserUpdateException("User with id " + id + " not exist"));
    }

    private void validImmutableFields(User existUser, User user){
        if(!existUser.getUserName().equals(user.getUserName())) {
            throw new InvalidUserUpdateException("UserName cannot be change");
        }
    }
}
