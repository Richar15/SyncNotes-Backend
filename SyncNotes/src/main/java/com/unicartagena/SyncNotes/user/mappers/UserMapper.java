package com.unicartagena.SyncNotes.user.mappers;

import com.unicartagena.SyncNotes.user.dto.UserDto;
import com.unicartagena.SyncNotes.user.dto.UserUpdateDto;
import com.unicartagena.SyncNotes.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    User toUserEntity(UserDto dto);
    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);
}
