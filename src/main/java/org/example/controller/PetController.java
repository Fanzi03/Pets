package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.example.controller.util.ControllerHelper;
import org.example.dto.PetDataTransferObject;
import org.example.service.PetService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController implements ControllerHelper{

    @Qualifier("petCacheService")
    private final PetService petService;

    @GetMapping
    public ResponseEntity<Page<PetDataTransferObject>> getAllPets(
        @RequestParam(value = "page",defaultValue = "0") int page,
        @RequestParam(value = "size",defaultValue = "10") int size
    ){
        return ResponseEntity.ok(petService.getPets(PageRequest.of(page, size))); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDataTransferObject> getPetById(@PathVariable("id") Long id){
        PetDataTransferObject pet = petService.findById(id);
        return ResponseEntity.ok(pet);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> addPet(@RequestBody @Valid PetDataTransferObject pet){
        PetDataTransferObject createdPet = petService.add(pet);
        Map<String, Object> response = returnResponse(
            "Pet with id: " + createdPet.getId() + " added", createdPet, HttpStatus.CREATED
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePet(@PathVariable("id") Long id){
        PetDataTransferObject delPetDataTransferObject = petService.findById(id);
        petService.delete(id);
        Map<String, Object> response = returnResponse(
            "Pet with id: " + id + " deleted", delPetDataTransferObject, HttpStatus.ACCEPTED
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePet(@PathVariable("id") Long id, @RequestBody @Valid PetDataTransferObject petUpdate){
        PetDataTransferObject petDtoUpadate = petService.update(id, petUpdate);
        Map<String, Object> response = returnResponse(
            "Pet with id: " + id + " updated", petDtoUpadate, HttpStatus.ACCEPTED
        );
    	return ResponseEntity.ok(response);
    }
}
