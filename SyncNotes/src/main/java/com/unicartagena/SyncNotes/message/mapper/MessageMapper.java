package com.unicartagena.SyncNotes.message.mapper;

import com.unicartagena.SyncNotes.message.dto.MessageDto;
import com.unicartagena.SyncNotes.message.entity.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageDto toDto(Message message);
    Message toEntity(MessageDto messageDto);
}
