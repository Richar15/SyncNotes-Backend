package com.unicartagena.SyncNotes.room.entity;

import lombok.Data;

@Data
public class RoomMember {
    private String userId;
    private String username;
    private RoomRole role;

    public enum RoomRole {
        OWNER,
        EDITOR,
        VIEWER
    }
}