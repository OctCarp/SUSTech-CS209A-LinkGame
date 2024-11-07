package io.github.octcarp.linkgame.server.net;

import io.github.octcarp.linkgame.common.module.Player;
import io.github.octcarp.linkgame.common.packet.SimpStatus;
import io.github.octcarp.linkgame.server.utils.FileIO;
import io.github.octcarp.linkgame.server.utils.ServerConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayersManager {
    private static PlayersManager instance = new PlayersManager();

    private CopyOnWriteArrayList<Player> playerList;

    private Map<String, ClientHandlerThread> onlinePlayers;

    private PlayersManager() {
        playerList = FileIO.readPlayerList();
        onlinePlayers = new ConcurrentHashMap<>();
    }

    public static PlayersManager getInstance() {
        return instance;
    }

    public SimpStatus playerCanLogin(Player loginPlayer) {
        if (!onlinePlayers.containsKey(loginPlayer.id())) {
            for (Player player : playerList) {
                if (player.id().equals(loginPlayer.id())) {
                    return player.password().equals(loginPlayer.password()) ?
                            SimpStatus.OK : SimpStatus.UNAUTHORIZED;
                }
            }
            return SimpStatus.NOT_FOUND;
        } else {
            return SimpStatus.CONFLICT;
        }
    }

    public SimpStatus playerLogout(String id) {
        if (onlinePlayers.containsKey(id)) {
            onlinePlayers.remove(id);
            removePlayerThread(id);
            return SimpStatus.OK;
        }
        return SimpStatus.NOT_FOUND;
    }

    public synchronized SimpStatus registerPlayer(Player newPlayer) {
        String id = newPlayer.id();
        if (ServerConfig.getReservedIds().contains(id)) {
            return SimpStatus.FORBIDDEN;
        }
        for (Player player : playerList) {
            if (player.id().equals(id)) {
                return SimpStatus.CONFLICT;
            }
        }
        playerList.add(newPlayer);
        if (!FileIO.updatePlayerByList(playerList)) {
            return SimpStatus.FAILURE;
        }
        return SimpStatus.OK;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public ClientHandlerThread getPlayerThread(String id) {
        return onlinePlayers.get(id);
    }

    public void addPlayerThread(String id, ClientHandlerThread thread) {
        onlinePlayers.put(id, thread);
    }

    public void removePlayerThread(String id) {
        onlinePlayers.remove(id);
    }
}
