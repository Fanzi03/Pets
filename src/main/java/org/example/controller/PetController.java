package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetDataTransferObject;
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
    public List<PetDataTransferObject> getAllPets(){
        return petService.getPets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDataTransferObject> getPetById(@PathVariable Long id){
        PetDataTransferObject pet = petService.findById(id).orElseThrow(()  -> new NoSuchElementException("Pet with id: " + id + " not found"));
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    public ResponseEntity<PetDataTransferObject> addPet(@RequestBody PetDataTransferObject pet){
        PetDataTransferObject createdPet = petService.add(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id){
        petService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDataTransferObject> updatePet(@PathVariable Long id, @RequestBody PetDataTransferObject petUpdate){
        return petService.update(id, petUpdate).
                map(ResponseEntity::ok).
                orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
