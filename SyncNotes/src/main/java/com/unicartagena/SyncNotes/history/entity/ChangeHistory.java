package com.unicartagena.SyncNotes.history.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "change_history")
@Data
public class ChangeHistory {
    @Id
    private String id;
    private String roomId;
    private String userId;
    private String username;
    private String action;
    private String entityType;
    private String entityId;
    private String description;
    private LocalDateTime timestamp;
}
