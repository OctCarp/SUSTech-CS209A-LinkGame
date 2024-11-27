package io.github.octcarp.sustech.cs209a.linkgame.client.utils

import io.github.octcarp.sustech.cs209a.linkgame.client.net.LoginData
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.io.IOException

object SceneSwitcher {
    const val TITLE_BASE = "CS209A Linking Game"

    private lateinit var primaryStage: Stage

    private val scenes = HashMap<String, Scene>()
    private val controllers = HashMap<String, Any>()


    fun setPrimaryStage(stage: Stage) {
        this.primaryStage = stage
    }

    fun updateGlobalTitle() {
        updateGlobalTitle(null)
    }

    fun updateGlobalTitle(addOn: String?) {
        Platform.runLater {
            primaryStage.title = buildString {
                append(TITLE_BASE)
                LoginData.currentPlayer?.id?.let { append(" - Player: $it") }
                addOn?.takeIf { it.isNotEmpty() }?.let { append(" - $it") }
            }
        }
    }

    fun switchScene(sceneName: String) {
        runCatching {
            val loader = FXMLLoader(javaClass.getResource("/views/$sceneName.fxml"))
            val root = loader.load<Parent?>()
            scenes[sceneName] = Scene(root)
            controllers[sceneName] = loader.getController<Any?>()
        }.onFailure {
            if (it is IOException) {
                it.printStackTrace()
            } else {
                throw RuntimeException(it)
            }
        }
        Platform.runLater {
            primaryStage.scene = scenes.get(sceneName)
        }
    }

    fun getController(sceneName: String): Any? {
        return controllers[sceneName]
    }

    fun netErrAndReturn() {
        AlertPopper.popNetErrAndToLogin()
    }
}
