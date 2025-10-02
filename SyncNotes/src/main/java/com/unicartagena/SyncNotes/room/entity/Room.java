package com.unicartagena.SyncNotes.room.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "rooms")
@Data
public class Room {
    @Id
    private String id;
    private String name;
    private String description;
    private String creatorId;
    private boolean isPublic;
    private LocalDateTime createdAt;
    private List<RoomMember> members = new ArrayList<>();
}