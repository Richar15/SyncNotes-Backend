package com.unicartagena.SyncNotes.task.mapper;

import com.unicartagena.SyncNotes.task.dto.TaskDto;
import com.unicartagena.SyncNotes.task.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(TaskDto taskDto);
}
