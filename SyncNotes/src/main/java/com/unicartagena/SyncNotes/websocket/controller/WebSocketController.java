package com.unicartagena.SyncNotes.websocket.controller;

import com.unicartagena.SyncNotes.message.dto.MessageDto;
import com.unicartagena.SyncNotes.message.service.MessageService;
import com.unicartagena.SyncNotes.task.dto.TaskDto;
import com.unicartagena.SyncNotes.user.entity.User;
import com.unicartagena.SyncNotes.websocket.dto.ChatMessage;
import com.unicartagena.SyncNotes.websocket.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/room/{roomId}/chat")
    public void sendMessage(@DestinationVariable String roomId,
                           @Payload ChatMessage chatMessage,
                           @AuthenticationPrincipal User user) {
        MessageDto savedMessage = messageService.saveMessage(roomId, chatMessage.getContent(), user.getId());
        WebSocketMessage wsMessage = new WebSocketMessage("CHAT_MESSAGE", savedMessage);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, wsMessage);
    }

    @MessageMapping("/room/{roomId}/task-update")
    public void taskUpdate(@DestinationVariable String roomId,
                          @Payload TaskDto taskDto) {
        WebSocketMessage wsMessage = new WebSocketMessage("TASK_UPDATE", taskDto);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, wsMessage);
    }

    @MessageMapping("/room/{roomId}/task-create")
    public void taskCreate(@DestinationVariable String roomId,
                          @Payload TaskDto taskDto) {
        WebSocketMessage wsMessage = new WebSocketMessage("TASK_CREATE", taskDto);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, wsMessage);
    }

    @MessageMapping("/room/{roomId}/task-delete")
    public void taskDelete(@DestinationVariable String roomId,
                          @Payload String taskId) {
        WebSocketMessage wsMessage = new WebSocketMessage("TASK_DELETE", taskId);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, wsMessage);
    }
}
