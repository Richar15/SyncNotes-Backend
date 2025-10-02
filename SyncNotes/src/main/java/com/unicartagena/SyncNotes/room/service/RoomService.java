package com.unicartagena.SyncNotes.room.service;

import com.unicartagena.SyncNotes.history.entity.ChangeHistory;
import com.unicartagena.SyncNotes.history.repository.ChangeHistoryRepository;
import com.unicartagena.SyncNotes.room.dto.AddMemberRequest;
import com.unicartagena.SyncNotes.room.dto.CreateRoomRequest;
import com.unicartagena.SyncNotes.room.dto.RoomDto;
import com.unicartagena.SyncNotes.room.entity.Room;
import com.unicartagena.SyncNotes.room.entity.RoomMember;
import com.unicartagena.SyncNotes.room.mapper.RoomMapper;
import com.unicartagena.SyncNotes.room.repository.RoomRepository;
import com.unicartagena.SyncNotes.user.entity.User;
import com.unicartagena.SyncNotes.user.exception.GlobalException;
import com.unicartagena.SyncNotes.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final UserRepository userRepository;
    private final ChangeHistoryRepository changeHistoryRepository;

    public RoomDto createRoom(CreateRoomRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        Room room = new Room();
        room.setName(request.getName());
        room.setDescription(request.getDescription());
        room.setPublic(request.isPublic());
        room.setCreatorId(userId);
        room.setCreatedAt(LocalDateTime.now());

        RoomMember owner = new RoomMember();
        owner.setUserId(userId);
        owner.setUsername(user.getUsername());
        owner.setRole(RoomMember.RoomRole.OWNER);

        List<RoomMember> members = new ArrayList<>();
        members.add(owner);
        room.setMembers(members);

        Room savedRoom = roomRepository.save(room);

        logChange(savedRoom.getId(), userId, user.getUsername(), "CREAR_SALA", "Room",
                  savedRoom.getId(), "Sala creada: " + savedRoom.getName());

        return roomMapper.toDto(savedRoom);
    }

    public List<RoomDto> getPublicRooms() {
        return roomRepository.findByIsPublicTrue().stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RoomDto> getUserRooms(String userId) {
        return roomRepository.findByMembers_UserId(userId).stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    public RoomDto getRoomById(String roomId, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        validateUserInRoom(room, userId);
        return roomMapper.toDto(room);
    }

    public RoomDto addMember(String roomId, AddMemberRequest request, String requesterId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        validateUserIsOwner(room, requesterId);

        User newMember = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GlobalException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        boolean alreadyMember = room.getMembers().stream()
                .anyMatch(m -> m.getUserId().equals(request.getUserId()));

        if (alreadyMember) {
            throw new GlobalException("El usuario ya es miembro de la sala", HttpStatus.BAD_REQUEST);
        }

        RoomMember member = new RoomMember();
        member.setUserId(request.getUserId());
        member.setUsername(newMember.getUsername());
        member.setRole(request.getRole());

        room.getMembers().add(member);
        Room savedRoom = roomRepository.save(room);

        User requester = userRepository.findById(requesterId).orElseThrow();
        logChange(roomId, requesterId, requester.getUsername(), "AGREGAR_MIEMBRO", "Room",
                  roomId, "Miembro agregado: " + newMember.getUsername() + " con rol " + request.getRole());

        return roomMapper.toDto(savedRoom);
    }

    public RoomDto updateMemberRole(String roomId, String memberId, RoomMember.RoomRole role, String requesterId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        validateUserIsOwner(room, requesterId);

        RoomMember member = room.getMembers().stream()
                .filter(m -> m.getUserId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new GlobalException("Miembro no encontrado", HttpStatus.NOT_FOUND));

        if (member.getRole() == RoomMember.RoomRole.OWNER) {
            throw new GlobalException("No se puede cambiar el rol del propietario", HttpStatus.BAD_REQUEST);
        }

        member.setRole(role);
        Room savedRoom = roomRepository.save(room);

        User requester = userRepository.findById(requesterId).orElseThrow();
        logChange(roomId, requesterId, requester.getUsername(), "ACTUALIZAR_ROL", "Room",
                  roomId, "Rol actualizado para " + member.getUsername() + " a " + role);

        return roomMapper.toDto(savedRoom);
    }

    public void validateUserInRoom(Room room, String userId) {
        boolean isMember = room.getMembers().stream()
                .anyMatch(m -> m.getUserId().equals(userId));

        if (!isMember) {
            throw new GlobalException("No tienes acceso a esta sala", HttpStatus.FORBIDDEN);
        }
    }

    public void validateUserCanEdit(Room room, String userId) {
        RoomMember member = room.getMembers().stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new GlobalException("No tienes acceso a esta sala", HttpStatus.FORBIDDEN));

        if (member.getRole() == RoomMember.RoomRole.VIEWER) {
            throw new GlobalException("No tienes permisos de edición", HttpStatus.FORBIDDEN);
        }
    }

    public void validateUserIsOwner(Room room, String userId) {
        RoomMember member = room.getMembers().stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new GlobalException("No tienes acceso a esta sala", HttpStatus.FORBIDDEN));

        if (member.getRole() != RoomMember.RoomRole.OWNER) {
            throw new GlobalException("Solo el propietario puede realizar esta acción", HttpStatus.FORBIDDEN);
        }
    }

    private void logChange(String roomId, String userId, String username, String action,
                          String entityType, String entityId, String description) {
        ChangeHistory history = new ChangeHistory();
        history.setRoomId(roomId);
        history.setUserId(userId);
        history.setUsername(username);
        history.setAction(action);
        history.setEntityType(entityType);
        history.setEntityId(entityId);
        history.setDescription(description);
        history.setTimestamp(LocalDateTime.now());
        changeHistoryRepository.save(history);
    }
}
