package com.unicartagena.SyncNotes.security.login.dto;

import com.unicartagena.SyncNotes.room.dto.RoomDto;
import com.unicartagena.SyncNotes.task.dto.TaskDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String message;
    private UserInfo user;
    private List<RoomDto> rooms;
    private List<TaskDto> tasks;

    @Data
    @Builder
    public static class UserInfo {
        private String id;
        private String name;
        private String username;
    }
}
