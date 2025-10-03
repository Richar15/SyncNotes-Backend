package com.unicartagena.SyncNotes.task.service;

import com.unicartagena.SyncNotes.history.entity.ChangeHistory;
import com.unicartagena.SyncNotes.history.repository.ChangeHistoryRepository;
import com.unicartagena.SyncNotes.room.entity.Room;
import com.unicartagena.SyncNotes.room.repository.RoomRepository;
import com.unicartagena.SyncNotes.room.service.RoomService;
import com.unicartagena.SyncNotes.task.dto.CreateTaskRequest;
import com.unicartagena.SyncNotes.task.dto.TaskDto;
import com.unicartagena.SyncNotes.task.dto.UpdateTaskRequest;
import com.unicartagena.SyncNotes.task.entity.Task;
import com.unicartagena.SyncNotes.task.mapper.TaskMapper;
import com.unicartagena.SyncNotes.task.repository.TaskRepository;
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
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final UserRepository userRepository;
    private final ChangeHistoryRepository changeHistoryRepository;

    public TaskDto createTask(String roomId, CreateTaskRequest request, String userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        roomService.validateUserCanEdit(room, userId);

        User user = userRepository.findById(userId).orElseThrow();

        Task task = new Task();
        task.setRoomId(roomId);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);
        task.setCreatedBy(userId);
        task.setCreatedByUsername(user.getUsername());
        task.setCreatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);

        logChange(roomId, userId, user.getUsername(), "CREAR_TAREA", "Task",
                  savedTask.getId(), "Tarea creada: " + savedTask.getTitle());

        return taskMapper.toDto(savedTask);
    }

    public List<TaskDto> getRoomTasks(String roomId, String userId, Boolean completed) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        roomService.validateUserInRoom(room, userId);

        List<Task> tasks;
        if (completed != null) {
            tasks = taskRepository.findByRoomIdAndCompleted(roomId, completed);
        } else {
            tasks = taskRepository.findByRoomId(roomId);
        }

        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskDto updateTask(String taskId, UpdateTaskRequest request, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new GlobalException("Tarea no encontrada", HttpStatus.NOT_FOUND));

        Room room = roomRepository.findById(task.getRoomId())
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        roomService.validateUserCanEdit(room, userId);

        User user = userRepository.findById(userId).orElseThrow();

        StringBuilder changes = new StringBuilder();

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
            changes.append("título, ");
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
            changes.append("descripción, ");
        }
        if (request.getCompleted() != null) {
            task.setCompleted(request.getCompleted());
            changes.append(request.getCompleted() ? "marcada como completada, " : "marcada como pendiente, ");
        }

        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdatedBy(userId);
        task.setUpdatedByUsername(user.getUsername());

        Task savedTask = taskRepository.save(task);

        if (changes.length() > 0) {
            String changeDesc = changes.substring(0, changes.length() - 2);
            logChange(task.getRoomId(), userId, user.getUsername(), "ACTUALIZAR_TAREA", "Task",
                      taskId, "Tarea actualizada (" + changeDesc + "): " + task.getTitle());
        }

        return taskMapper.toDto(savedTask);
    }

    public void deleteTask(String taskId, String userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new GlobalException("Tarea no encontrada", HttpStatus.NOT_FOUND));

        Room room = roomRepository.findById(task.getRoomId())
                .orElseThrow(() -> new GlobalException("Sala no encontrada", HttpStatus.NOT_FOUND));

        // Validar que solo el creador de la tarea pueda eliminarla
        if (!task.getCreatedBy().equals(userId)) {
            throw new GlobalException("Solo el creador de la tarea puede eliminarla", HttpStatus.FORBIDDEN);
        }

        User user = userRepository.findById(userId).orElseThrow();

        logChange(task.getRoomId(), userId, user.getUsername(), "ELIMINAR_TAREA", "Task",
                  taskId, "Tarea eliminada: " + task.getTitle());

        taskRepository.delete(task);
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
