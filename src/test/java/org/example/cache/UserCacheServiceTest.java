package org.example.cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.dto.UserDataTransferObject;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.cache.type=SIMPLE",
    "spring.data.redis.host=localhost",
    "spring.data.redis.port=6379"
})
@FieldDefaults(level = AccessLevel.PRIVATE) 
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserCacheServiceTest {

    @Autowired
    @Qualifier("userCacheService")
    UserService userService;

    @Autowired
    @MockitoSpyBean
    UserRepository userRepository;

    // @Test
    // @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    // void findUserById(){
    //     Long id = 100L;
    //     UserDataTransferObject userDataTransferObject = 
    //         userService.findUserById(id);         
    //     assertNotNull(userDataTransferObject, "user not null"); 

    //     UserDataTransferObject cachedUserDataTransferObject = userService.findUserById(id);
    //     assertNotNull(cachedUserDataTransferObject, "cachedUser not null");

    //     verify(userRepository, times(1)).findById(id);
    // }
}
