package io.github.octcarp.sustech.cs209a.linkgame.server.net;

import io.github.octcarp.sustech.cs209a.linkgame.common.model.Game;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Response;
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.ResponseType;
import io.github.octcarp.sustech.cs209a.linkgame.server.model.MatchInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchManager {
    private static final MatchManager instance = new MatchManager();

    private final Map<String, MatchInfo> matches;

    private MatchManager() {
        matches = new ConcurrentHashMap<>();
    }

    public static MatchManager getInstance() {
        return instance;
    }

    public void createMatch(String playerId, String oppId) {
        Match match = new Match(playerId, oppId);
        ClientHandlerThread p1Thread = LobbyManager.getInstance().getClientThreadByPlayerId(playerId);
        ClientHandlerThread p2Thread = LobbyManager.getInstance().getClientThreadByPlayerId(oppId);
        MatchInfo matchInfo = new MatchInfo(match, playerId, oppId, p1Thread, p2Thread);
        matches.put(playerId, matchInfo);
        matches.put(oppId, matchInfo);

        Response response = new Response(ResponseType.START_MATCH);
        response.setData(match);

        p1Thread.sendResponse(response);
        p2Thread.sendResponse(response);
    }

    public void selectBoard(String playerId, GridPos boardSize) {
        MatchInfo matchInfo = matches.get(playerId);
        if (matchInfo == null) {
            return;
        }
        Match match = matchInfo.getMatch();
        match.initGame(boardSize.row(), boardSize.col());

        Match finalMatch = match.copy();
        finalMatch.getGame().setBoard(match.getGame().getBoard());
        Response response = new Response(ResponseType.SYNC_MATCH);
        response.setData(finalMatch);

        matchInfo.getP1Thread().sendResponse(response);
        matchInfo.getP2Thread().sendResponse(response);

        matchInfo.setMatch(finalMatch);
    }

    public void judgeTurnMove(String playerId, GridPos start, GridPos end) {
        MatchInfo matchInfo = matches.get(playerId);
        if (matchInfo == null) {
            return;
        }
        if (!playerId.equals(matchInfo.getMatch().getCurTurn())) {
            return;
        }
        matchInfo.judgeMoveAndUpdate(playerId, start, end);
        Match finalMatch = matchInfo.getMatch().copy();
        Response response = new Response(ResponseType.SYNC_MATCH);
        response.setData(finalMatch);
        matchInfo.getP1Thread().sendResponse(response);
        matchInfo.getP2Thread().sendResponse(response);
        matchInfo.setMatch(finalMatch);
        if (matchInfo.getMatch().getLastPath().size() >= 2) {
            matchInfo.getMatch().getGame().clearGrids(start.row(), start.col(), end.row(), end.col());
            boolean isGameFinish = matchInfo.getMatch().getGame().gameFinished();
            if (isGameFinish) {
                gameFinish(matchInfo);
            }
        }
    }

    private void gameFinish(MatchInfo matchInfo) {
        Response gameOverResponse = new Response(ResponseType.MATCH_FINISHED);
        Match finalMatch = matchInfo.getMatch().copy();
        gameOverResponse.setData(finalMatch);
        matchInfo.getP1Thread().sendResponse(gameOverResponse);
        matchInfo.getP2Thread().sendResponse(gameOverResponse);
        matches.remove(matchInfo.getP1());
        matches.remove(matchInfo.getP2());
    }

    public void playerDisconnected(String playerId) {
        MatchInfo matchInfo = matches.get(playerId);
        if (matchInfo != null) {
            ClientHandlerThread oppThread = matchInfo.getP1().equals(playerId) ?
                    matchInfo.getP2Thread() : matchInfo.getP1Thread();
            Response response = new Response(ResponseType.OPP_DISCONNECTED);
            oppThread.sendResponse(response);
            matches.remove(matchInfo.getP1());
            matches.remove(matchInfo.getP2());
        }
    }

    public void shuffleBoard(String playerId) {
        MatchInfo matchInfo = matches.get(playerId);
        if (matchInfo == null) {
            return;
        }
        if (!playerId.equals(matchInfo.getMatch().getCurTurn())) {
            return;
        }
        matchInfo.getMatch().getGame().shuffleBoard();

        Game game = matchInfo.getMatch().getGame().copy();
        Response response = new Response(ResponseType.SYNC_BOARD);
        response.setData(game);

        matchInfo.getP1Thread().sendResponse(response);
        matchInfo.getP2Thread().sendResponse(response);
    }

//    public void addMatch(Match match) {
//        matches.add(match);
//    }
//
//    public void removeMatch(Match match) {
//        matches.remove(match);
//    }
//
//    public List<Match> getMatches() {
//        return matches;
//    }
}
