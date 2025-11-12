package com.unicartagena.SyncNotes.room.controller;

import com.unicartagena.SyncNotes.room.dto.AddMemberRequest;
import com.unicartagena.SyncNotes.room.dto.CreateRoomRequest;
import com.unicartagena.SyncNotes.room.dto.RoomDto;
import com.unicartagena.SyncNotes.room.entity.RoomMember;
import com.unicartagena.SyncNotes.room.service.RoomService;
import com.unicartagena.SyncNotes.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody CreateRoomRequest request,
                                              @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(roomService.createRoom(request, user.getId()));
    }

    @GetMapping("/public")
    public ResponseEntity<List<RoomDto>> getPublicRooms() {
        return ResponseEntity.ok(roomService.getPublicRooms());
    }

    @GetMapping("/my-rooms")
    public ResponseEntity<List<RoomDto>> getMyRooms(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(roomService.getUserRooms(user.getId()));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable String roomId,
                                               @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(roomService.getRoomById(roomId, user.getId()));
    }

    @PostMapping("/{roomId}/members")
    public ResponseEntity<RoomDto> addMember(@PathVariable String roomId,
                                             @Valid @RequestBody AddMemberRequest request,
                                             @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(roomService.addMember(roomId, request, user.getId()));
    }

    @PutMapping("/{roomId}/members/{memberId}/role")
    public ResponseEntity<RoomDto> updateMemberRole(@PathVariable String roomId,
                                                    @PathVariable String memberId,
                                                    @RequestParam RoomMember.RoomRole role,
                                                    @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(roomService.updateMemberRole(roomId, memberId, role, user.getId()));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String roomId,
                                           @AuthenticationPrincipal User user) {
        roomService.deleteRoom(roomId, user.getId());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{roomId}/active-users")
    public ResponseEntity<List<User>> getActiveUsers(@PathVariable String roomId,
                                                     @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(roomService.getActiveUsers(roomId, user.getId()));
}
}
