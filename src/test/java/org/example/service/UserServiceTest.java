package org.example.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.example.mapping.PetMapper;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceTest {
    
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock 
    PetMapper petMapper;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void getUserTest(){
	Long userId = 1L;

	User user = new User(
        userId, "user_name", "full_name", "email", "password", 16, null
    );
	UserDataTransferObject userDataTransferObject = new UserDataTransferObject(
        userId,"user_name", "full_name", "email", "password", 16)
    ;

	when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	when(userMapper.toDTO(user)).thenReturn(userDataTransferObject);

    UserDataTransferObject result = userService.findUserById(userId);
	assertEquals(userDataTransferObject, result);
	verify(userMapper, times(1)).toDTO(user);
    } 
}

