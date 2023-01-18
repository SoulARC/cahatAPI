package chatApi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String message;
    private String sender;
    private MessageType chatType;
}
