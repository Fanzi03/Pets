package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.example.dto.PetDataTransferObject;
import org.example.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<Page<PetDataTransferObject>> getAllPets(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(petService.getPets(PageRequest.of(page, size))); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDataTransferObject> getPetById(@PathVariable Long id){
        PetDataTransferObject pet = petService.findById(id);
        return ResponseEntity.ok(pet);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String,Object>> addPet(@RequestBody @Valid PetDataTransferObject pet){
        PetDataTransferObject createdPet = petService.add(pet);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pet with id: " + createdPet.getId() + " added");
        response.put("addedPet", createdPet);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePet(@PathVariable Long id){
        PetDataTransferObject delPetDataTransferObject = petService.findById(id);
        petService.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pet with id: " + id + " deleted");
        response.put("deletedPet", delPetDataTransferObject);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePet(@PathVariable Long id, @RequestBody @Valid PetDataTransferObject petUpdate){
        PetDataTransferObject petDtoUpadate = petService.update(id, petUpdate);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Pet with id: " + id + " updated");
        response.put("updatedPet", petDtoUpadate);
        response.put("status", "success");
    	return ResponseEntity.ok(response);
    }
}
