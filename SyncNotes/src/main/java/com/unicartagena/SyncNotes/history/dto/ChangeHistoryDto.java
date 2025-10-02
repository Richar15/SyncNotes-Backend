package com.unicartagena.SyncNotes.history.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChangeHistoryDto {
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
