package org.example.mapping;

import org.example.dto.UserDataTransferObject;
import org.example.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserMapperTest {
    @Mock
    PetMapper petMapper;

    @InjectMocks
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testEntityToDTO() {
        Long userId = 1L;

        User user = new User(
                userId, "user_name", "full_name", "email", "password", 16, null);

        UserDataTransferObject userDataTransferObject = userMapper.toDTO(user);

        assertEquals(userDataTransferObject.getId(), userId);
        assertEquals(userDataTransferObject.getUserName(), user.getUserName());
        assertEquals(userDataTransferObject.getFullName(), user.getFullName());
        assertEquals(userDataTransferObject.getEmail(), user.getEmail());
        // assertEquals(userDataTransferObject.getPassword(), user.getPassword());
        assertEquals(userDataTransferObject.getAge(), user.getAge());
    }

    @Test
    void testDTOtoEntity() {
        UserDataTransferObject userDataTransferObject = new UserDataTransferObject(
                "user_name", "full_name", "email", "password", 16);

        User user = userMapper.toEntity(userDataTransferObject);

        assertEquals(user.getUserName(), userDataTransferObject.getUserName());
        assertEquals(user.getFullName(), userDataTransferObject.getFullName());
        assertEquals(user.getEmail(), userDataTransferObject.getEmail());
        // assertEquals(user.getPassword(), userDataTransferObject.getPassword());
        assertEquals(user.getAge(), userDataTransferObject.getAge());
    }
}
