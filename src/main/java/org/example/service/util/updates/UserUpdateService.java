package org.example.service.util.updates;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.dto.UserDataTransferObject;
import org.example.exception.InvalidUserUpdateException;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserUpdateService {
    UserRepository userRepository;
    UserMapper userMapper;
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
                        existUser.setPassword(userDataTransferObject.getPassword());
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
