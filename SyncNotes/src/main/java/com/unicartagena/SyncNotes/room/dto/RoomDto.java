package com.unicartagena.SyncNotes.room.dto;

import com.unicartagena.SyncNotes.room.entity.RoomMember;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoomDto {
    private String id;
    private String name;
    private String description;
    private String creatorId;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private List<RoomMember> members;
}
