package pl.howkymike.model;

import java.io.Serializable;

public class Message implements Serializable {
    private final MessageType messageType;
    private final Object data;

    public Message(MessageType messageType, Object data) {
        this.messageType = messageType;
        this.data = data;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", data=" + data +
                '}';
    }
}
