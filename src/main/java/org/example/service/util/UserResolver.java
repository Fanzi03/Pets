package org.example.service.util;

import org.example.entity.User;
import org.example.exception.custom.NotFoundUserException;
import org.example.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResolver {
    UserRepository userRepository;

    public User resolveUser(String ownerName){
        if(ownerName != null && !ownerName.isBlank()){
            return userRepository.findByUserName(ownerName)
                    .orElseThrow(() -> new NotFoundUserException("User with userName " + ownerName + " not found"));
        }
        return null;
    }
}
