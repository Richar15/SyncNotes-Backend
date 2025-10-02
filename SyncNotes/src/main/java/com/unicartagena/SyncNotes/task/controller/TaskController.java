package com.unicartagena.SyncNotes.task.controller;

import com.unicartagena.SyncNotes.task.dto.CreateTaskRequest;
import com.unicartagena.SyncNotes.task.dto.TaskDto;
import com.unicartagena.SyncNotes.task.dto.UpdateTaskRequest;
import com.unicartagena.SyncNotes.task.service.TaskService;
import com.unicartagena.SyncNotes.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms/{roomId}/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@PathVariable String roomId,
                                              @Valid @RequestBody CreateTaskRequest request,
                                              @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.createTask(roomId, request, user.getId()));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getRoomTasks(@PathVariable String roomId,
                                                      @RequestParam(required = false) Boolean completed,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.getRoomTasks(roomId, user.getId(), completed));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable String roomId,
                                              @PathVariable String taskId,
                                              @Valid @RequestBody UpdateTaskRequest request,
                                              @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request, user.getId()));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String roomId,
                                           @PathVariable String taskId,
                                           @AuthenticationPrincipal User user) {
        taskService.deleteTask(taskId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
