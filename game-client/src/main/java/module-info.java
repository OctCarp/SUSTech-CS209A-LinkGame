module io.github.octcarp.linkgame.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.github.octcarp.linkgame.client to javafx.fxml;
    exports io.github.octcarp.linkgame.client;
}