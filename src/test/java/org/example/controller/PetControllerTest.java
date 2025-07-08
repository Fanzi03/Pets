package org.example.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.controller.util.GettingAccessToken;
import org.example.dto.PetDataTransferObject;
import org.example.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)
@TestPropertySource(properties = {
    "VALIDATION_ANIMAL_TYPES=dog,cat" 
})
@AutoConfigureMockMvc
public class PetControllerTest implements GettingAccessToken{
    
    @Autowired
    @Getter
    MockMvc mockMvc;
    
    @Autowired
    @Getter
    ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql"})
    void addPetTestWithoutOwner() throws Exception{
        PetDataTransferObject petDataTransferObject = PetDataTransferObject.builder().name("pet12").age(1)
            .type("dog").gender(Gender.MAN).ownerName(null).build();
        
        String petJson = objectMapper.writeValueAsString(petDataTransferObject);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/pets/add").contentType(MediaType.APPLICATION_JSON)
            .content(petJson)
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk())
            .andExpect(jsonPath("$.addedPet.id").value(1));
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql", "/data/insertPetData.sql"})
    void getPetByIdTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/100")
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("pet1"));
    }

    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql", "/data/insertPetData.sql"})
    void getAllPetsTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/pets").param("size", "10")
            .param("page", "0").header("Authorization", "Bearer " + getTestAccessToken()))
            .andExpect(status().isOk());
    }
    
    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql", "/data/insertPetData.sql"})
    void deletePetTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/pets/100")
            .header("Authorization", "Bearer " + getTestAccessToken())).andExpect(status().isOk())
            .andExpect(jsonPath("$.deletedPet.age").value(1));
    }


    @Test
    @Sql(scripts = {"/data/cleanUp.sql", "/data/insertData.sql", "/data/insertPetData.sql"})
    void updatePetTest() throws Exception{
        PetDataTransferObject petDataTransferObject = PetDataTransferObject.builder().name("pet113345").age(1)
            .type("dog").gender(Gender.MAN).ownerName("FANZI03").build(); 

        String petJson = objectMapper.writeValueAsString(petDataTransferObject);

        mockMvc.perform(MockMvcRequestBuilders.put("/pets/100").contentType(MediaType.APPLICATION_JSON)
            .content(petJson).header("Authorization", "Bearer " + getTestAccessToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.updatedPet.ownerName")
            .value(petDataTransferObject.getOwnerName()));
    }
}
