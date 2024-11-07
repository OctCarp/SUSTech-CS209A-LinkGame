package io.github.octcarp.linkgame.common.packet;

import java.io.Serializable;

public class Request implements Serializable {
    private String sender;
    private RequestType type;
    private Object data;

    public Request(RequestType type) {
        this.sender = null;
        this.type = type;
        this.data = null;
    }

    public Request(String sender, RequestType type, Object data) {
        this.sender = sender;
        this.type = type;
        this.data = data;
    }

    public String getSender() {
        return sender;
    }

    public RequestType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
