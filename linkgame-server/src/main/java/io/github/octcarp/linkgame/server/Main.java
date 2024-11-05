package io.github.octcarp.linkgame.server;


public class Main {
    public static void main(String[] args) {
        int port = 28080;
        Server server = new Server(port);

        server.startServer();
    }
}