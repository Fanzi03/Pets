package org.example.kafka;

import org.example.entity.Pet;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PetKafkaProducer {

    KafkaTemplate<String, Pet> kafkaTemplate;

    public void sendPetToKafka(Pet pet){
        String key = "pet-" + pet.getId();
        kafkaTemplate.send("pets", key, pet);
    }

    
}
