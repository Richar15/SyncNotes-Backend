package com.unicartagena.SyncNotes.user.service;

import com.unicartagena.SyncNotes.user.dto.UserDto;
import com.unicartagena.SyncNotes.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto updateUser(String id, UserUpdateDto userUpdateDto);
    void deleteUser(String id);
}
