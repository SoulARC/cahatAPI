package chatApi.config;

import chatApi.model.MessageType;
import chatApi.storage.Storage;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import chatApi.model.ChatMessage;

import java.util.Objects;

@Component
public class SocketAction {
    private final SimpMessageSendingOperations messagingTemplate;

    public SocketAction(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Is connected");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = (String)
                Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        if(userName != null){
            System.out.printf("%s is not connected%n", userName);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatType(MessageType.LEAVE);
            chatMessage.setSender(userName);
            Storage.removeBySession(headerAccessor.getSessionId());
            messagingTemplate.convertAndSend("/topic/all",chatMessage);
        }
    }
}
