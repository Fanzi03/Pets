package org.example.service.util.add;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.UserDataTransferObject;
import org.example.dto.mapping.UserMapper;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.service.UserService.ALREADY_EXISTS_MESSAGE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCreateService {
    UserMapper userMapper;
    UserRepository userRepository;

    @Transactional
    public UserDataTransferObject create (UserDataTransferObject userDataTransferObject) {
        if(userRepository.existsByEmail(userDataTransferObject.getEmail())) {
            throw new IllegalArgumentException("Email: " +
                    userDataTransferObject.getEmail() + ALREADY_EXISTS_MESSAGE);
        }

        if (userRepository.existsByUserName(userDataTransferObject.getUserName())) {
            throw new IllegalArgumentException("Username: " + userDataTransferObject.getUserName() +
                    ALREADY_EXISTS_MESSAGE);
        }


        User userEntity = userMapper.toEntity(userDataTransferObject);
        return userMapper.toDTO(userRepository.save(userEntity));
    }
}
