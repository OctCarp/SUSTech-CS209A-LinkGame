package io.github.octcarp.linkgame.server.net;

import io.github.octcarp.linkgame.common.packet.Response;
import io.github.octcarp.linkgame.common.packet.ResponseType;

import java.util.HashMap;
import java.util.Map;

public class LobbyManager {
    private static LobbyManager instance;

    private final Map<String, ClientHandlerThread> lobbyPlayerThreads;
    private final Map<String, String> waitingPlayers;

    private LobbyManager() {
        lobbyPlayerThreads = new HashMap<>();
        waitingPlayers = new HashMap<>();
    }

    public static LobbyManager getInstance() {
        if (instance == null) {
            instance = new LobbyManager();
        }
        return instance;
    }

    public ClientHandlerThread getClientThreadByPlayerId(String playerId) {
        return lobbyPlayerThreads.get(playerId);
    }

    public void enterLobby(String playerId, ClientHandlerThread clientHandlerThread) {
        lobbyPlayerThreads.put(playerId, clientHandlerThread);
        notifyAllLobbyPlayers();
    }

    public void exitLobby(String playerId) {
        lobbyPlayerThreads.remove(playerId);
        notifyAllLobbyPlayers();
    }

    public void notifyAllLobbyPlayers() {
        Response response = new Response(ResponseType.ALL_MATCHING_PLAYER, lobbyPlayerThreads.keySet());
        for (ClientHandlerThread clientHandlerThread : lobbyPlayerThreads.values()) {
            clientHandlerThread.sendResponse(response);
        }
    }

}
