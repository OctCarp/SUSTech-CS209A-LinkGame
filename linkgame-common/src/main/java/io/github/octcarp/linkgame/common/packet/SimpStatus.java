package io.github.octcarp.linkgame.common.packet;

public enum SimpStatus {
    OK(200, "Request succeeded"),
    FAILURE(500, "Internal server error"),
    TIMEOUT(408, "Request timeout"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not found"),
    CONFLICT(409, "Conflict"),
    LOCKED(423, "Resource Locked"),
    ;

    private int code;
    private String message;

    SimpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
