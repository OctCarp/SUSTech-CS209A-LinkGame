package io.github.octcarp.linkgame.common.packet;

import java.io.Serializable;

public class Request implements Serializable {
    private final String sender;
    private final RequestType type;
    private final String content;

    public Request(String sender, RequestType type, String content) {
        this.sender = sender;
        this.type = type;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public RequestType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
