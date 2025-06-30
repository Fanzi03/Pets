package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.PetDataTransferObject;
import org.example.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/addPet")
    public ResponseEntity<PetDataTransferObject> addPet(@RequestBody @Valid PetDataTransferObject pet){
        PetDataTransferObject createdPet = petService.add(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id){
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDataTransferObject> updatePet(@PathVariable Long id, @RequestBody @Valid PetDataTransferObject petUpdate){
        PetDataTransferObject petDtoUpadate = petService.update(id, petUpdate);
	return ResponseEntity.ok().body(petDtoUpadate);
    }

}
