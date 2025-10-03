package com.unicartagena.SyncNotes.security.login.service;

import com.unicartagena.SyncNotes.room.dto.RoomDto;
import com.unicartagena.SyncNotes.room.mapper.RoomMapper;
import com.unicartagena.SyncNotes.room.repository.RoomRepository;
import com.unicartagena.SyncNotes.security.login.dto.LoginRequest;
import com.unicartagena.SyncNotes.security.login.dto.LoginResponse;
import com.unicartagena.SyncNotes.security.login.utils.JwtUtil;
import com.unicartagena.SyncNotes.task.dto.TaskDto;
import com.unicartagena.SyncNotes.task.mapper.TaskMapper;
import com.unicartagena.SyncNotes.task.repository.TaskRepository;
import com.unicartagena.SyncNotes.user.entity.User;
import com.unicartagena.SyncNotes.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final TaskRepository taskRepository;
    private final RoomMapper roomMapper;
    private final TaskMapper taskMapper;

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

            // Cargar todos los datos del usuario
            List<RoomDto> userRooms = roomRepository.findByMembers_UserId(user.getId())
                    .stream()
                    .map(roomMapper::toDto)
                    .collect(Collectors.toList());

            // Obtener todas las tareas de los rooms del usuario
            List<TaskDto> userTasks = userRooms.stream()
                    .flatMap(room -> taskRepository.findByRoomId(room.getId()).stream())
                    .map(taskMapper::toDto)
                    .collect(Collectors.toList());

            LoginResponse loginResponse = LoginResponse.builder()
                    .token(token)
                    .message("Inicio de sesión exitoso")
                    .user(LoginResponse.UserInfo.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .username(user.getUsername())
                            .build())
                    .rooms(userRooms)
                    .tasks(userTasks)
                    .build();

            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            response.put("error", "Error en el servidor");
            return ResponseEntity.status(500).body(response);
        }
    }

    public ResponseEntity<?> getUserData(String username) {
        Map<String, Object> response = new HashMap<>();

        try {
            var existingUser = userRepository.findByUsername(username);

            if (existingUser.isEmpty()) {
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.status(404).body(response);
            }

            User user = existingUser.get();

            // Cargar todos los datos del usuario
            List<RoomDto> userRooms = roomRepository.findByMembers_UserId(user.getId())
                    .stream()
                    .map(roomMapper::toDto)
                    .collect(Collectors.toList());

            // Obtener todas las tareas de los rooms del usuario
            List<TaskDto> userTasks = userRooms.stream()
                    .flatMap(room -> taskRepository.findByRoomId(room.getId()).stream())
                    .map(taskMapper::toDto)
                    .collect(Collectors.toList());

            LoginResponse userData = LoginResponse.builder()
                    .token(null) // No enviamos token en /me
                    .message("Datos obtenidos exitosamente")
                    .user(LoginResponse.UserInfo.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .username(user.getUsername())
                            .build())
                    .rooms(userRooms)
                    .tasks(userTasks)
                    .build();

            return ResponseEntity.ok(userData);

        } catch (Exception e) {
            response.put("error", "Error en el servidor");
            return ResponseEntity.status(500).body(response);
        }
    }

}
