package com.unicartagena.SyncNotes.websocket.controller;

import com.unicartagena.SyncNotes.message.dto.MessageDto;
import com.unicartagena.SyncNotes.message.service.MessageService;
import com.unicartagena.SyncNotes.task.dto.TaskDto;
import com.unicartagena.SyncNotes.user.entity.User;
import com.unicartagena.SyncNotes.user.repository.UserRepository;
import com.unicartagena.SyncNotes.websocket.dto.ChatMessage;
import com.unicartagena.SyncNotes.websocket.dto.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserRepository userRepository;

    /**
     * Publica un mensaje de chat en la sala.
     * Cliente: publish a /app/room/{roomId}/chat   con body: { "content": "..." }
     * Suscripción de clientes: /topic/room/{roomId}
     */
    @MessageMapping("/room/{roomId}/chat")
    public void sendMessage(@DestinationVariable String roomId,
                            @Payload ChatMessage chatMessage,
                            Principal principal) {

        // 1) Usuario autenticado del canal WS (viene del interceptor JWT)
        //    principal.getName() == username del JWT
        String username = (principal != null) ? principal.getName() : null;

        if (username == null || username.isBlank()) {
            // Si por alguna razón no hay principal, no continuamos para evitar NPE
            return;
        }

        // 2) Buscamos la entidad User para obtener el ID (MongoRepository<String>)
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) {
            return; // usuario no encontrado; evita NPE
        }

        // 3) Guardamos y emitimos
        MessageDto savedMessage = messageService.saveMessage(roomId, chatMessage.getContent(), user.getId());
        WebSocketMessage wsMessage = new WebSocketMessage("CHAT_MESSAGE", savedMessage);

        // 4) Broadcast a la sala
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