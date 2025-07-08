package org.example.controller.util;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.dto.JwtAuthenticationDto;
import org.example.dto.UserCredentialsDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface GettingAccessToken {
    
    ObjectMapper getObjectMapper();

    MockMvc getMockMvc();

    default String getTestAccessToken() throws Exception{
        UserCredentialsDto UserCredentialsDto = org.example.dto.UserCredentialsDto.builder()
            .email("workemail@gmail.com").password("12345").build();

        String loginJson = getObjectMapper().writeValueAsString(UserCredentialsDto);

        String tokens = getMockMvc().perform(MockMvcRequestBuilders.post("/auth/sign-in")
            .contentType(MediaType.APPLICATION_JSON).content(loginJson)).andExpect(status().isOk()).andReturn()
            .getResponse().getContentAsString();
            
        JwtAuthenticationDto jwtAuthenticationDto = getObjectMapper().readValue(
            tokens, JwtAuthenticationDto.class
        );

        return jwtAuthenticationDto.getToken();
    }
}
