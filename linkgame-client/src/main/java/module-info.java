module linkgame.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive linkgame.common;

    opens io.github.octcarp.linkgame.client to javafx.fxml;
    exports io.github.octcarp.linkgame.client;

    opens io.github.octcarp.linkgame.client.controller to javafx.fxml;
    exports io.github.octcarp.linkgame.client.controller;

    opens io.github.octcarp.linkgame.client.net to javafx.fxml;
    exports io.github.octcarp.linkgame.client.net;
}