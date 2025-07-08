package org.example.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.controller.util.GettingAccessToken;
import org.example.dto.UserDataTransferObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserControllerTest implements GettingAccessToken{

    @Autowired
    @Getter
    MockMvc mockMvc;

    @Autowired
    @Getter
    ObjectMapper objectMapper;

    @Test
    void createUserWithoutPetsTest() throws Exception{
        UserDataTransferObject userDataTransferObject = UserDataTransferObject.builder()
            .fullName("anton1132").age(14).email("anton6411650190@gmail.com").userName("anton3335")
                .password("646453450190").build();

        String userJson = objectMapper.writeValueAsString(userDataTransferObject);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/registration")
            .contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isOk());
    }

    
    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void userGetUserByIdTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users/100")
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("workemail@gmail.com"));
    }


    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void getUserByEmail() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/workemail@gmail.com")
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("workemail@gmail.com"));
    }


    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void userUpdateTest() throws Exception{
            UserDataTransferObject userDataTransferObject = UserDataTransferObject.builder()
                .email("workemail@gmail.com").age(16).fullName("Fanzi03")
                .userName("FANZI03")
                .password("123456789stupidValid)))").build();

            String userJson = objectMapper.writeValueAsString(userDataTransferObject);

            mockMvc.perform(MockMvcRequestBuilders.put("/users/100")
                .contentType(MediaType.APPLICATION_JSON).content(userJson)
                .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk())
                .andExpect(jsonPath("$.updatedUser.email").value("workemail@gmail.com"));
    }
    
    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void getUsers() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users").param("page", "0")
            .param("size", "10")
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void getUserPets() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/users/100/pets")
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk());
    }


    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void deleteUser() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/100")
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk())
            .andExpect(jsonPath("$.deletedUser.email").value("workemail@gmail.com"));
    }
}
