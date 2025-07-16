package org.example.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

import org.example.controller.util.ControllerHelper;
import org.example.dto.PetDataTransferObject;
import org.example.dto.UserDataTransferObject;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements ControllerHelper {

    @Qualifier("userCacheService")
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDataTransferObject>> getUsers(
        @RequestParam(value = "page",defaultValue = "0") int page,
        @RequestParam(value = "size",defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/{id}/pets")
    public ResponseEntity<List<PetDataTransferObject>> getUserPets(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.getUserPets(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDataTransferObject> getUserById(@PathVariable("id") Long id) {
        UserDataTransferObject user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDataTransferObject> getUserByEmail(
        @PathVariable("email") @Email(message = "Invalid email format") String email
    ){
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }
    
    @GetMapping("username/{username}")
    public ResponseEntity<UserDataTransferObject> getUserByUserName(
        @PathVariable("username") String userName 
    ){
        return ResponseEntity.ok(userService.findUserByUserName(userName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,Object>> userUpdate(
        @PathVariable("id") Long id, @RequestBody @Valid UserDataTransferObject userUpdate
    ) {
        UserDataTransferObject userdtoUpdate = userService.update(userUpdate, id);
        Map<String,Object> response = returnResponse(
            "User with id: " + id + " updated", userdtoUpdate, HttpStatus.ACCEPTED
        );
	    return ResponseEntity.ok(response);           
    }


    @PostMapping("/registration")
    public ResponseEntity<Map<String,Object>> createUser(@RequestBody @Valid UserDataTransferObject user) {
        UserDataTransferObject userCreated = userService.createUser(user);
        Map<String,Object> response = returnResponse(
            "User with id: " + userCreated.getId() + " added", userCreated, HttpStatus.CREATED
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,Object>> deleteUser(@PathVariable("id") Long id) {
       UserDataTransferObject deletedUserDataTransferObject = userService.findUserById(id);
       userService.deleteById(id);
       Map<String,Object> response = returnResponse(
            "User with id: " + id + " deleted", deletedUserDataTransferObject, HttpStatus.ACCEPTED
       );
       return ResponseEntity.ok(response);
    }
}
