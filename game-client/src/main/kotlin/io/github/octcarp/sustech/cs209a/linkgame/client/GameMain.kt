package io.github.octcarp.sustech.cs209a.linkgame.client

import io.github.octcarp.sustech.cs209a.linkgame.client.net.ClientService
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.WindowEvent
import java.util.*
import java.util.function.Consumer
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    Application.launch(GameMain::class.java, *args)
}

class GameMain : Application() {
    override fun init() {
    }

    override fun start(primaryStage: Stage) {
        // init primary stage settings
        initPrimaryStage(primaryStage)

        // show login scene finally
        SceneSwitcher.setPrimaryStage(primaryStage)
        SceneSwitcher.switchScene("login") // Default login scene
        primaryStage.show()
    }

    private fun initPrimaryStage(primaryStage: Stage) {
        // set window title & icon
        primaryStage.icons.add(
            Image(
                Objects.requireNonNull(
                    GameMain::class.java.getResource(
                        "/img/main_icon.png"
                    )
                ).toExternalForm()
            )
        )
        primaryStage.title = "CS209A Linking Game"

        // handle the close event
        primaryStage.onCloseRequest = EventHandler { event: WindowEvent ->
            event.consume()
            Alert(Alert.AlertType.CONFIRMATION).apply {
                title = "Confirm Exit"
                headerText = "Are you sure you want to exit?"
                contentText = "Your match step will be lost."
            }.showAndWait().ifPresent(Consumer { response: ButtonType ->
                if (response == ButtonType.OK) {
                    myExit()
                }
            })
        }
    }

    private fun myExit() {
        AlertPopper.tryExit = true

        try {
            val request = Request(RequestType.SHUTDOWN)
            ClientService.sendRequest(request)
        } catch (_: Exception) {
            // do nothing
        } finally {
            Platform.exit()
            exitProcess(0)
        }
    }
}