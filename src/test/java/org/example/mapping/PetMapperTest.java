package org.example.mapping;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.example.dto.PetDataTransferObject;
import org.example.entity.Pet;
import org.example.enums.Gender;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetMapperTest {
    @Mock
    UserMapper userMapper;

    PetMapper petMapper = Mappers.getMapper(PetMapper.class);

    @Test
    void toDto() {
        Long petId = 1L;

        Pet pet = Pet.builder().id(petId).name("PET1").type("dog")
        .gender(Gender.MAN).age(15).build();

        PetDataTransferObject petDto = petMapper.toDTO(pet);

        assertThat(petDto.getName(), equalTo(pet.getName()));
        assertThat(petDto.getId(), equalTo(pet.getId()));
        assertThat(petDto.getType(), equalTo(pet.getType()));
        assertThat(petDto.getGender(), equalTo(pet.getGender()));
        assertThat(petDto.getAge(), equalTo(pet.getAge()));
    }

    @Test
    void toEntity() {
        PetDataTransferObject petDto = PetDataTransferObject.builder().name("PET1").type("dog")
            .gender(Gender.MAN).age(15).build();

        Pet pet = petMapper.toEntity(petDto);

        assertThat(pet.getName(), equalTo(petDto.getName()));
        assertThat(pet.getId(), equalTo(petDto.getId()));
        assertThat(pet.getType(), equalTo(petDto.getType()));
        assertThat(pet.getGender(), equalTo(petDto.getGender()));
        assertThat(pet.getAge(), equalTo(petDto.getAge()));
    }
}
