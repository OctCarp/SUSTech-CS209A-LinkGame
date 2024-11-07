package io.github.octcarp.linkgame.server.net;

import io.github.octcarp.linkgame.common.module.Player;
import io.github.octcarp.linkgame.common.packet.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandlerThread implements Runnable {

    private final Socket socket;
    private Player player;
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;

    public ClientHandlerThread(Socket accept) {
        this.socket = accept;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object oin = ois.readObject();
                if (!(oin instanceof Request request)) {
                    throw new RuntimeException("Invalid request");
                }
                RequestType typeIn = request.getType();
                switch (typeIn) {
                    case LOGIN -> {
                        Player player = (Player) request.getData();
                        SimpStatus status = PlayersManager.getInstance().playerCanLogin(player);

                        if (status == SimpStatus.OK) {
                            this.player = player;
                            PlayersManager.getInstance().addPlayerThread(player.id(), this);
                        }

                        Response response = new Response(ResponseType.LOGIN_RESULT);
                        response.setData(status);
                        oos.writeObject(response);
                    }
                    case REGISTER -> {
                        Player newPlayer = (Player) request.getData();
                        SimpStatus status = PlayersManager.getInstance().registerPlayer(newPlayer);

                        Response response = new Response(ResponseType.REGISTER_RESULT);
                        response.setData(status);
                        oos.writeObject(response);
                    }
                    case LOGOUT -> {
                        SimpStatus status = PlayersManager.getInstance().playerLogout(player.id());

                        Response response = new Response(ResponseType.LOGOUT_RESULT);
                        response.setData(status);
                        oos.writeObject(response);
                    }
                    default -> {
                        if (player == null) {
                            Response response = new Response(ResponseType.ERROR_MESSAGE);
                            response.setData("Please login first");
                            oos.writeObject(response);
                        } else {
                            switch (typeIn) {
                                case ENTER_LOBBY -> {
                                    LobbyManager.getInstance().enterLobby(player.id(), this);
                                }
                                case EXIT_LOBBY -> {
                                    LobbyManager.getInstance().exitLobby(player.id());
                                }
                                default -> {
                                    // send response to client
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    if (player != null) {
                        PlayersManager.getInstance().playerLogout(player.id());
                    }
                    System.out.println("Client:" + socket.getRemoteSocketAddress() + " disconnected");
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendResponse(Response response) {
        try {
            oos.writeObject(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
