package io.github.octcarp.linkgame.common.packet;

import java.io.Serializable;

public class Response implements Serializable {
    private final String sender;
    private final ResponseType type;
    private final Object data;

    public Response(String sender, ResponseType type, Object data) {
        this.sender = sender;
        this.type = type;
        this.data = data;
    }

    public String getSender() {
        return sender;
    }

    public ResponseType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }
}
