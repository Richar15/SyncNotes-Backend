package com.unicartagena.SyncNotes.room.mapper;

import com.unicartagena.SyncNotes.room.dto.RoomDto;
import com.unicartagena.SyncNotes.room.entity.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDto toDto(Room room);
    Room toEntity(RoomDto roomDto);
}
