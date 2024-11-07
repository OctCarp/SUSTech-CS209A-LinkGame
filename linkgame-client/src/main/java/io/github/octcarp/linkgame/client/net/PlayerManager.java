package io.github.octcarp.linkgame.client.net;

import io.github.octcarp.linkgame.client.controller.LoginController;
import io.github.octcarp.linkgame.client.controller.MainMenuController;
import io.github.octcarp.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.linkgame.common.module.Player;
import io.github.octcarp.linkgame.common.packet.*;

public class PlayerManager {
    private static PlayerManager instance = null;
    private Player currentPlayer;
    private Player tempPlayer;

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public void playerRegister(Player player) {
        Request request = new Request(RequestType.REGISTER);
        request.setData(player);
        ClientService.getInstance().sendRequest(request);
    }

    public void rePlayerRegister(SimpStatus status) {
        LoginController controller = (LoginController)
                SceneSwitcher.getInstance().getController("login");
        controller.handleRegisterResult(status);
    }

    public void playerLogin(Player player) {
        Request request = new Request("default", RequestType.LOGIN, player);
        tempPlayer = player;
        ClientService.getInstance().sendRequest(request);
    }

    public void rePlayerLogin(SimpStatus status) {
        if (status == SimpStatus.OK) {
            setCurrentPlayer(tempPlayer);
        }
        LoginController controller = (LoginController)
                SceneSwitcher.getInstance().getController("login");
        controller.handleLoginResult(status);
    }

    public void logout() {
        if (currentPlayer == null) {
            return;
        }

        Request request = new Request(RequestType.LOGOUT);
        request.setData(currentPlayer);
        ClientService.getInstance().sendRequest(request);
    }

    public void reLogout(SimpStatus status) {
        setCurrentPlayer(null);
        if (status == SimpStatus.OK) {
            setCurrentPlayer(null);
        }
        MainMenuController controller = (MainMenuController)
                SceneSwitcher.getInstance().getController("main-menu");
        controller.handleLogoutResult(status);
    }

    private void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
