package com.unicartagena.SyncNotes.message.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {
    private String id;
    private String roomId;
    private String userId;
    private String username;
    private String content;
    private LocalDateTime timestamp;
}
