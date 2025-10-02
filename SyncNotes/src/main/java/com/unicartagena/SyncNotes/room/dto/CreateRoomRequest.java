package com.unicartagena.SyncNotes.room.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoomRequest {
    @NotBlank(message = "El nombre es requerido")
    private String name;
    private String description;
    private boolean isPublic;
}
