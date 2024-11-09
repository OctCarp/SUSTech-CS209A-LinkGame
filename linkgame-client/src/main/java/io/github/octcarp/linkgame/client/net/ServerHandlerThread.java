package io.github.octcarp.linkgame.client.net;

import io.github.octcarp.linkgame.common.model.Game;
import io.github.octcarp.linkgame.common.model.Match;
import io.github.octcarp.linkgame.common.packet.Response;
import io.github.octcarp.linkgame.common.packet.SimpStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class ServerHandlerThread implements Runnable {
    private final Socket socket;
    private final ObjectInputStream ois;

    public ServerHandlerThread(Socket socket, ObjectInputStream ois) {
        this.socket = socket;
        try {
            this.ois = ois;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object oin = ois.readObject();
                if (!(oin instanceof Response)) {
                    continue;
                }
                Response response = (Response) oin;
                switch (response.getType()) {
                    case LOGIN_RESULT -> {
                        SimpStatus status = (SimpStatus) response.getData();
                        LoginData.getInstance().rePlayerLogin(status);
                    }
                    case REGISTER_RESULT -> {
                        SimpStatus status = (SimpStatus) response.getData();
                        LoginData.getInstance().rePlayerRegister(status);
                    }
                    case LOGOUT_RESULT -> {
                        SimpStatus status = (SimpStatus) response.getData();
                        LoginData.getInstance().reLogout(status);
                    }
                    case ALL_WAITING_PLAYERS -> {
                        List<String> players = (List<String>) response.getData();
                        LobbyData.getInstance().reAllWaitingPlayers(players);
                    }
                    case START_MATCH -> {
                        Match match = (Match) response.getData();
                        MatchData.getInstance().reStartMatch(match);
                    }
                    case SYNC_MATCH -> {
                        Match match = (Match) response.getData();
                        MatchData.getInstance().reSyncMatch(match);
                    }
                    case SYNC_BOARD -> {
                        Game game = (Game) response.getData();
                        MatchData.getInstance().reSyncBoard(game);
                    }
                    case MATCH_FINISHED -> {
                        Match match = (Match) response.getData();
                        MatchData.getInstance().reMatchFinished(match);
                    }
                }
            }
        } catch (IOException e) {
//            SceneSwitcher.getInstance().netErrAndReturn();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e);
        } finally {
            ClientService.getInstance().disconnect();
        }
    }
}
