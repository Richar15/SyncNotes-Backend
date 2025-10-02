package com.unicartagena.SyncNotes.task.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {
    private String id;
    private String roomId;
    private String title;
    private String description;
    private boolean completed;
    private String createdBy;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String updatedByUsername;
}
