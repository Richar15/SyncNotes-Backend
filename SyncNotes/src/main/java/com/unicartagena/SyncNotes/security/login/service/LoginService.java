package com.unicartagena.SyncNotes.security.login.service;

import com.unicartagena.SyncNotes.security.login.dto.LoginRequest;
import com.unicartagena.SyncNotes.security.login.utils.JwtUtil;
import com.unicartagena.SyncNotes.user.entity.User;
import com.unicartagena.SyncNotes.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> login(LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {

            var existingUser = userRepository.findByUsername(request.getUsername());
            var userWithPasswordExists = userRepository.findByUsername(request.getUsername())
                    .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                    .isPresent();

            if (!existingUser.isPresent() && !userWithPasswordExists) {
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.status(401).body(response);
            }

            if (existingUser.isEmpty()) {
                response.put("error", "Usuario incorrecto");
                return ResponseEntity.status(401).body(response);
            }

            User user = existingUser.get();


            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                response.put("error", "Usuario incorrecto");
                return ResponseEntity.status(401).body(response);
            }


            String token = jwtUtil.generateToken(user.getUsername());
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("message", "Inicio de sesión exitoso");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Error en el servidor");
            return ResponseEntity.status(500).body(response);
        }
    }

}
