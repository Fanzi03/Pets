package org.example.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.example.service.UserService;
import org.example.dto.UserDataTransferObject;
import org.example.dto.util.UserDataTransferObjectWithPetList;
import org.example.entity.User;
import org.example.mapping.UserMapper;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceTest {
    
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;


    @Test
    void getUserTest(){
	Long userId = 1L;

	User user = new User(userId, "user_name", "full_name", "email", "password", 16, null);
	UserDataTransferObject userDataTransferObject = new UserDataTransferObject(userId,"user_name", "full_name", "email", "password", 16);
//	UserDataTransferObjectWithPetList userDataTransferObjectWithPetList = 
//	    new UserDataTransferObjectWithPetList();

	when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	when(userMapper.toDTO(user)).thenReturn(userDataTransferObject);
//	when(userMapper.toDTOWithPetList(user))
//	    .thenReturn(userDataTransferObjectWithPetList);

	Optional<UserDataTransferObject> result = userService.findUserById(userId);
//	Optional<UserDataTransferObjectWithPetList> result2 = 
//	    userService.findUserById(userId);

	assertTrue(result.isPresent());
	assertEquals(userDataTransferObject, result.get());
//	assertEquals(userDataTransferObjectWithPetList, result2.get());

	verify(userMapper, times(1)).toDTO(user);



    } 

}

