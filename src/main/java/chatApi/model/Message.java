package chatApi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String counter;
    private String sender;
    private MessageType type;
}
