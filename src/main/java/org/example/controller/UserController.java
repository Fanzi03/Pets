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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        return ResponseEntity.ok(userService.findUserByEmailWithCache(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,Object>> userUpdate(@PathVariable Long id, @RequestBody @Valid UserDataTransferObject userUpdate) {
        UserDataTransferObject userdtoUpdate = userService.update(userUpdate, id);
        Map<String,Object> response = new HashMap<>();
        response.put("message", "User with id: " + id + " updated");
        response.put("updatedUser", userdtoUpdate);
        response.put("status", "success");
	    return ResponseEntity.ok(response);           
    }


    @PostMapping("/registration")
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody @Valid UserDataTransferObject user) {
        UserDataTransferObject userCreated = userService.createUser(user);
        Map<String,Object> response = new HashMap<>();
        response.put("message", "User with id: " + userCreated.getId() + " added");
        response.put("addedUser", userCreated);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteUser(@PathVariable Long id) {
       UserDataTransferObject deletedUserDataTransferObject = userService.findUserById(id);
       userService.deleteById(id);
       Map<String,Object> response = new HashMap<>();
       response.put("message", "User with id: " + id + " deleted");
       response.put("deletedUser", deletedUserDataTransferObject);
       response.put("status", "success");
       return ResponseEntity.ok(response);
    }
}
