package com.unicartagena.SyncNotes.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTaskRequest {
    @NotBlank(message = "El título es requerido")
    private String title;
    private String description;
}
