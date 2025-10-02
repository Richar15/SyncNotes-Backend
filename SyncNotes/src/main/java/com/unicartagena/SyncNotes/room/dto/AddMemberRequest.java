package com.unicartagena.SyncNotes.room.dto;

import com.unicartagena.SyncNotes.room.entity.RoomMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddMemberRequest {
    @NotBlank(message = "El ID de usuario es requerido")
    private String userId;
    @NotNull(message = "El rol es requerido")
    private RoomMember.RoomRole role;
}
