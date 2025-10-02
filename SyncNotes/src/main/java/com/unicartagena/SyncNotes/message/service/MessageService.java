package com.unicartagena.SyncNotes.message.service;

import com.unicartagena.SyncNotes.message.dto.MessageDto;
import com.unicartagena.SyncNotes.message.entity.Message;
import com.unicartagena.SyncNotes.message.mapper.MessageMapper;
import com.unicartagena.SyncNotes.message.repository.MessageRepository;
import com.unicartagena.SyncNotes.room.entity.Room;
import com.unicartagena.SyncNotes.room.repository.RoomRepository;
import com.unicartagena.SyncNotes.room.service.RoomService;
import com.unicartagena.SyncNotes.user.entity.User;
import com.unicartagena.SyncNotes.user.exception.GlobalException;
import com.unicartagena.SyncNotes.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final UserRepository userRepository;

    public MessageDto saveMessage(String roomId, String content, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        roomService.validateUserInRoom(room, userId);

        User user = userRepository.findById(userId).orElseThrow();

        Message message = new Message();
        message.setRoomId(roomId);
        message.setUserId(userId);
        message.setUsername(user.getUsername());
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        return messageMapper.toDto(savedMessage);
    }

    public List<MessageDto> getRoomMessages(String roomId, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        roomService.validateUserInRoom(room, userId);

        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId).stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }
}
