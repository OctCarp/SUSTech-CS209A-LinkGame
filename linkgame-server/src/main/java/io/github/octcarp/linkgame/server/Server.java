package io.github.octcarp.linkgame.server;

import io.github.octcarp.linkgame.common.module.PlayerRecord;
import io.github.octcarp.linkgame.server.utils.FileIO;

import java.util.ArrayList;
import java.util.List;


public class Server {

    private final int port;
    private final List<PlayerRecord> playerList = new ArrayList<>();


    public Server(int port) {
        this.port = port;

    }

    public void initServer() {
        FileIO.readPlayerList();
    }

    public void startServer() {
        initServer();
    }
}