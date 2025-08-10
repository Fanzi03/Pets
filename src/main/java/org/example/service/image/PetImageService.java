package org.example.service.image;

import org.springframework.stereotype.Service;
import org.example.dto.PetDataTransferObject;
//import org.example.entity.Pet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PetImageService {
  
  public String petToPromt(PetDataTransferObject pet){
    return "Please generate for me the pet with these characteristics, name: " + pet.getName() 
      + ", age: " + pet.getAge() + ", type: " + pet.getType() + ", gender: " + pet.getGender(); 
  }
    
}
