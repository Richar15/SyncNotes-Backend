package com.unicartagena.SyncNotes.history.mapper;

import com.unicartagena.SyncNotes.history.dto.ChangeHistoryDto;
import com.unicartagena.SyncNotes.history.entity.ChangeHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChangeHistoryMapper {
    ChangeHistoryDto toDto(ChangeHistory changeHistory);
    ChangeHistory toEntity(ChangeHistoryDto changeHistoryDto);
}
