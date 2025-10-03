package com.unicartagena.SyncNotes.security.login.controller;


import com.unicartagena.SyncNotes.security.login.dto.LoginRequest;
import com.unicartagena.SyncNotes.security.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login de usuarios")
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "Iniciar sesión",
            description = "Permite a un usuario iniciar sesión y obtener un token JWT con todos sus datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @Operation(summary = "Obtener información del usuario autenticado",
            description = "Retorna todos los datos del usuario basándose en el JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos obtenidos exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token inválido o expirado")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        return loginService.getUserData(authentication.getName());
    }
}
