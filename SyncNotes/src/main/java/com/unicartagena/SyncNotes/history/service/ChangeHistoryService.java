package com.unicartagena.SyncNotes.history.service;

import com.unicartagena.SyncNotes.history.dto.ChangeHistoryDto;
import com.unicartagena.SyncNotes.history.mapper.ChangeHistoryMapper;
import com.unicartagena.SyncNotes.history.repository.ChangeHistoryRepository;
import com.unicartagena.SyncNotes.room.entity.Room;
import com.unicartagena.SyncNotes.room.repository.RoomRepository;
import com.unicartagena.SyncNotes.room.service.RoomService;
import com.unicartagena.SyncNotes.user.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChangeHistoryService {
    private final ChangeHistoryRepository changeHistoryRepository;
    private final ChangeHistoryMapper changeHistoryMapper;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    public List<ChangeHistoryDto> getRoomHistory(String roomId, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        roomService.validateUserInRoom(room, userId);

        return changeHistoryRepository.findByRoomIdOrderByTimestampDesc(roomId).stream()
                .map(changeHistoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
