package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.dto.JwtAuthenticationDto;
import org.example.dto.RefreshTokenDto;
import org.example.dto.UserCredentialsDto;
import org.example.security.jwt.JwtService;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthControllerTest {
    

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;
    
    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    public void signInTest() throws Exception{

       UserCredentialsDto userCDto = UserCredentialsDto.builder().email("workemail@gmail.com")
        .password("12345").build();

        String userJson = objectMapper.writeValueAsString(userCDto); 

        String jsonToken = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JwtAuthenticationDto jwtAuthenticationDto = objectMapper.readValue(jsonToken, JwtAuthenticationDto.class);

        assertEquals(userCDto.getEmail(), jwtService.getEmailFromToken(jwtAuthenticationDto.getToken()));

        
    }; 


    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    public void signInNegativeTest() throws Exception{

       UserCredentialsDto userCDto = UserCredentialsDto.builder().email("workemail@gmail.com")
        .password("123455").build();

        String userJson = objectMapper.writeValueAsString(userCDto); 


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isUnauthorized());
    }; 

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    public void refresh() throws Exception{
        
       UserCredentialsDto userCDto = UserCredentialsDto.builder().email("workemail@gmail.com")
        .password("12345").build();

        String userJson = objectMapper.writeValueAsString(userCDto); 

        String jsonToken = mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    
        RefreshTokenDto refreshTokenDto = 
            RefreshTokenDto.builder().refreshToken(objectMapper.readValue(
                jsonToken, JwtAuthenticationDto.class).getRefreshToken()).build();
        
        String refreshTokenJson = objectMapper.writeValueAsString(refreshTokenDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh").contentType(
            MediaType.APPLICATION_JSON).content(refreshTokenJson)).andExpect(status().isOk());
    }
}
