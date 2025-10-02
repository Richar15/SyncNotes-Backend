package com.unicartagena.SyncNotes.user.controller;

import com.unicartagena.SyncNotes.user.dto.UserDto;
import com.unicartagena.SyncNotes.user.dto.UserUpdateDto;
import com.unicartagena.SyncNotes.user.exception.GlobalException;
import com.unicartagena.SyncNotes.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup-user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDto));
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.deleteUser(id);
            response.put("mensaje", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (GlobalException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
