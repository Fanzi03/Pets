package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Pet;
import org.example.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public List<Pet> getAllPets(){
        return petService.getPets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id){
        Pet pet = petService.findById(id).orElseThrow(()  -> new NoSuchElementException("Pet with id: " + id + " not found"));
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet){
        Pet createdPet = petService.add(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id){
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petUpdate){
        return petService.update(id, petUpdate).
                map(ResponseEntity::ok).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
