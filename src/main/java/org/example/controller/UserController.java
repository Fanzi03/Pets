package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PetDTO;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.service.UserService;

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
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}/pets")
    public List<PetDTO> getUserPets(@PathVariable Long id){
        return userService.getUserPets(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findUserById(id)
                .orElseThrow(() -> new NoSuchElementException("User with " + id + " not found"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> userUpdate(@PathVariable Long id, @RequestBody User userUpdate) {
        return userService.update(userUpdate, id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        UserDTO userCreated = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
       userService.deleteById(id);
       return ResponseEntity.noContent().build();
    }
}
