package com.unicartagena.SyncNotes.message.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Data
public class Message {
    @Id
    private String id;
    private String roomId;
    private String userId;
    private String username;
    private String content;
    private LocalDateTime timestamp;
}