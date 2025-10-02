package com.unicartagena.SyncNotes.task.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tasks")
@Data
public class Task {
    @Id
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