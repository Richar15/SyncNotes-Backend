package com.unicartagena.SyncNotes.user.mappers;

import com.unicartagena.SyncNotes.user.dto.UserDto;
import com.unicartagena.SyncNotes.user.dto.UserUpdateDto;
import com.unicartagena.SyncNotes.user.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T12:26:45-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( user.getId() );
        userDto.setName( user.getName() );
        userDto.setUsername( user.getUsername() );
        userDto.setPassword( user.getPassword() );

        return userDto;
    }

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setName( dto.getName() );
        user.setUsername( dto.getUsername() );
        user.setPassword( dto.getPassword() );

        return user;
    }

    @Override
    public void updateUserFromDto(UserUpdateDto dto, User user) {
        if ( dto == null ) {
            return;
        }

        user.setName( dto.getName() );
        user.setUsername( dto.getUsername() );
    }

    @Override
    public UserDto toDto(UserUpdateDto updateDto) {
        if ( updateDto == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setName( updateDto.getName() );
        userDto.setUsername( updateDto.getUsername() );

        return userDto;
    }
}
