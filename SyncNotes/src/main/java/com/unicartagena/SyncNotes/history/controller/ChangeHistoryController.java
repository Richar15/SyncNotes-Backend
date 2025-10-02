package com.unicartagena.SyncNotes.history.controller;

import com.unicartagena.SyncNotes.history.dto.ChangeHistoryDto;
import com.unicartagena.SyncNotes.history.service.ChangeHistoryService;
import com.unicartagena.SyncNotes.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms/{roomId}/history")
@RequiredArgsConstructor
public class ChangeHistoryController {
    private final ChangeHistoryService changeHistoryService;

    @GetMapping
    public ResponseEntity<List<ChangeHistoryDto>> getRoomHistory(@PathVariable String roomId,
                                                                 @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(changeHistoryService.getRoomHistory(roomId, user.getId()));
    }
}
