package org.example.service;

import java.util.Optional;
import java.time.Year;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.dto.PetDataTransferObject;
import org.example.dto.mapping.PetMapper;
import org.example.entity.Pet;
import org.example.entity.enums.Gender;
import org.example.repository.PetRepository;
import org.example.service.PetService;

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

	Optional<PetDataTransferObject> result = petService.findById(petId);
	
	assertTrue(result.isPresent());
	assertEquals(petDTO, result.get());

	verify(petMapper, times(1)).toDTO(pet);
    }
    
}
