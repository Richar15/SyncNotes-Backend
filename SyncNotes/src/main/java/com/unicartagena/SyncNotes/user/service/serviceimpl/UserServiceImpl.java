package com.unicartagena.SyncNotes.user.service.serviceimpl;

import com.unicartagena.SyncNotes.user.dto.UserDto;
import com.unicartagena.SyncNotes.user.dto.UserUpdateDto;
import com.unicartagena.SyncNotes.user.entity.User;
import com.unicartagena.SyncNotes.user.exception.GlobalException;
import com.unicartagena.SyncNotes.user.mappers.UserMapper;
import com.unicartagena.SyncNotes.user.repository.UserRepository;
import com.unicartagena.SyncNotes.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        userRepository.findByName(userDto.getName())
                .ifPresent(u -> {
                    throw new GlobalException("El nombre ya existe, por favor ingrese otro.");
                });
        userRepository.findByUsername(userDto.getUsername())
                .ifPresent(u -> {
                    throw new GlobalException("El nombre de usuario ya existe, por favor ingrese otro.");
                });

        User user = mapper.toUserEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User saved = userRepository.save(user);
        return mapper.toUserDto(saved);
    }

@Override
public UserDto updateUser(String id, UserUpdateDto userUpdateDto) {
    User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new GlobalException("Usuario no encontrado con ID: " + id));

    if (!existingUser.getName().equals(userUpdateDto.getName())) {
        userRepository.findByName(userUpdateDto.getName())
                .ifPresent(u -> {
                    throw new GlobalException("El nombre ya existe, por favor ingrese otro.");
                });
    }
    if (!existingUser.getUsername().equals(userUpdateDto.getUsername())) {
        userRepository.findByUsername(userUpdateDto.getUsername())
                .ifPresent(u -> {
                    throw new GlobalException("El nombre de usuario ya existe, por favor ingrese otro.");
                });
    }
    mapper.updateUserFromDto(userUpdateDto, existingUser);
    User updatedUser = userRepository.save(existingUser);
    return mapper.toUserDto(updatedUser);
}

@Override
public void deleteUser(String id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new GlobalException("Usuario no encontrado con ID: " + id));

    userRepository.delete(user);
}
@Override
public Map<String, Object> findUserByUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
        throw new GlobalException("El username no puede estar vacío");
    }

    Map<String, Object> response = new HashMap<>();

    try {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GlobalException("No se encontró ningún usuario con el username proporcionado."));

        UserDto userDto = mapper.toUserDto(user);
        response.put("usuario", userDto);
        response.put("mensaje", "Usuario encontrado correctamente.");
        return response;

    } catch (GlobalException e) {
        throw e;
    } catch (Exception e) {
        throw new GlobalException("Ocurrió un error al buscar el usuario.");
    }
}
}
