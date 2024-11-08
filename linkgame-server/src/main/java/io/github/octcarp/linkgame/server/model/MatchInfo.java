package io.github.octcarp.linkgame.server.model;

import io.github.octcarp.linkgame.common.module.GridPos;
import io.github.octcarp.linkgame.common.module.Match;
import io.github.octcarp.linkgame.server.net.ClientHandlerThread;

import java.io.Serializable;
import java.util.List;

public class MatchInfo implements Serializable {
    private Match match;

    private final String p1;
    private final String p2;

    private final ClientHandlerThread p1Thread;
    private final ClientHandlerThread p2Thread;

    public MatchInfo(Match match, String p1, String p2, ClientHandlerThread p1Thread, ClientHandlerThread p2Thread) {
        this.match = match;
        this.p1 = p1;
        this.p2 = p2;
        this.p1Thread = p1Thread;
        this.p2Thread = p2Thread;
    }

//    public void syncMatch() {
//        Response response = new Response(ResponseType.SYNC_MATCH);
//        response.setData( getMatch());
//        player1Thread.sendResponse(response);
//        player2Thread.sendResponse(response);
//    }

    public void judgeMoveAndUpdate(String playerId, GridPos start, GridPos end) {
        Match match = getMatch();
        List<GridPos> path = match.getGame().judge(start.row(), start.col(), end.row(), end.col());
        if (path == null) {
            path = List.of();
        } else {
            path.addFirst(start);
            path.addLast(end);
            if (getP1().equals(playerId)) {
                match.incP1Score(1);
            } else {
                match.incP2Score(1);
            }
        }
        match.switchTurn();
        match.setLastPath(path);
    }

    public Match getMatch() {
        return match;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public ClientHandlerThread getP1Thread() {
        return p1Thread;
    }

    public ClientHandlerThread getP2Thread() {
        return p2Thread;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
