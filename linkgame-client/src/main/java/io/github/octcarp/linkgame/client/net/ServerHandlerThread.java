package io.github.octcarp.linkgame.client.net;

import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.linkgame.common.packet.Response;
import io.github.octcarp.linkgame.common.packet.SimpStatus;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
                        PlayerManager.getInstance().rePlayerLogin(status);
                    }
                    case REGISTER_RESULT -> {
                        SimpStatus status = (SimpStatus) response.getData();
                        PlayerManager.getInstance().rePlayerRegister(status);
                    }
                    case LOGOUT_RESULT -> {
                        SimpStatus status = (SimpStatus) response.getData();
                        PlayerManager.getInstance().reLogout(status);
                    }
                }
            }
        } catch (IOException e) {
//            SceneSwitcher.getInstance().netErrAndReturn();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }
}
