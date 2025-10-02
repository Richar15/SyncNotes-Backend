package com.unicartagena.SyncNotes.message.controller;

import com.unicartagena.SyncNotes.message.dto.MessageDto;
import com.unicartagena.SyncNotes.message.service.MessageService;
import com.unicartagena.SyncNotes.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms/{roomId}/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDto>> getRoomMessages(@PathVariable String roomId,
                                                            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(messageService.getRoomMessages(roomId, user.getId()));
    }
}
