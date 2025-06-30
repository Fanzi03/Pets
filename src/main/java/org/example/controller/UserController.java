package org.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDataTransferObject>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/{id}/pets")
    public ResponseEntity<List<PetDataTransferObject>> getUserPets(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserPets(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDataTransferObject> getUserById(@PathVariable Long id) {
        UserDataTransferObject user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDataTransferObject> getUserByEmail(@PathVariable @Email(message = "Invalid email format") String email){
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDataTransferObject> userUpdate(@PathVariable Long id, @RequestBody @Valid UserDataTransferObject userUpdate) {
        UserDataTransferObject userdtoUpdate = userService.update(userUpdate, id);
	return ResponseEntity.ok().body(userdtoUpdate);           
    }

    @PostMapping("/registration")
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
