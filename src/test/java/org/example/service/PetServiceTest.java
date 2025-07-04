package org.example.service;

import java.util.Optional;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.enums.Gender;
import org.example.mapping.PetMapper;
import org.example.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetServiceTest {
   
    @Mock
    PetRepository petRepository;

    @Mock 
    PetMapper petMapper;

    @InjectMocks
    PetService petService;

    @Test
    void getPetTest(){

	Long petId = 1L;
	
	Pet pet = new Pet(petId, "PET1", "parrot", Gender.MAN, 15, 2009, LocalDate.of(2025,6,10), null);
	PetDataTransferObject petDTO = new PetDataTransferObject(petId,"PET1", "parrot", Gender.MAN, 15, null);
	
	when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
	when(petMapper.toDTO(pet)).thenReturn(petDTO);

	PetDataTransferObject result = petService.findById(petId);
	
	assertEquals(petDTO, result);

	verify(petMapper, times(1)).toDTO(pet);
    }
    
}
