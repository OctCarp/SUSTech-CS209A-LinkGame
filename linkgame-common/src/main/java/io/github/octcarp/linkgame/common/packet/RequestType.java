package io.github.octcarp.linkgame.common.packet;

import java.io.Serializable;

public enum RequestType implements Serializable {
    REGISTER,
    LOGIN,
    LOGOUT,
    DISCONNECT,

    ENTER_LOBBY,
    EXIT_LOBBY,
    START_WAITING,
    STOP_WAITING,

    JOIN_PLAYER,

    SELECT_BOARD,
    EXIT_MATCH,
    SHUFFLE_BOARD,

    TURN_MOVE,
    ;
}
