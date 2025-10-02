package com.unicartagena.SyncNotes.task.dto;

import lombok.Data;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Boolean completed;
}
