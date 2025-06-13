package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.service.UserService;
import org.mapstruct.control.MappingControl.Use;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDataTransferObject> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}/pets")
    public List<PetDataTransferObject> getUserPets(@PathVariable Long id){
        return userService.getUserPets(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDataTransferObject> getUserById(@PathVariable Long id) {
        UserDataTransferObject user = userService.findUserById(id)
                .orElseThrow(() -> new NoSuchElementException("User with " + id + " not found"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDataTransferObject> userUpdate(@PathVariable Long id, @RequestBody @Valid UserDataTransferObject userUpdate) {
        UserDataTransferObject userdtoUpdate = userService.update(userUpdate, id);
	return ResponseEntity.ok().body(userdtoUpdate);           
    }

    @PostMapping
    public ResponseEntity<UserDataTransferObject> createUser(@RequestBody @Valid UserDataTransferObject user) {
        UserDataTransferObject userCreated = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
       userService.deleteById(id);
       return ResponseEntity.noContent().build();
    }
}
