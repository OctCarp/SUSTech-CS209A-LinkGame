package io.github.octcarp.linkgame.common.packet;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    LOGIN_RESULT,
    REGISTER_RESULT,
    LOGOUT_RESULT,

    ALL_WAITING_PLAYERS,
    WAITING_OPPONENT,

    START_MATCH,
    SYNC_MATCH,
    SYNC_BOARD,
    MATCH_FINISHED,

    OPP_DISCONNECTED,
    OPP_RECONNECTED,

    ERROR_MESSAGE,
    ;
}
