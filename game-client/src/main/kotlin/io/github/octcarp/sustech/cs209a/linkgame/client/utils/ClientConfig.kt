package io.github.octcarp.sustech.cs209a.linkgame.client.utils

import java.util.*

object ClientConfig {
    var serverAddress: String = "localhost"
        private set

    var serverPort: Int = 29901
        private set

    init {
        runCatching {
            ClientConfig::class.java.getResourceAsStream("/client_config.properties")?.use { input ->
                Properties().apply {
                    load(input)

                    serverAddress = getProperty("server.addr", serverAddress)
                    serverPort = getProperty("server.port", serverPort.toString()).toInt()

                    require(serverPort in 1..65535) { "Port must be between 1 and 65535" }
                }
            } ?: throw IllegalStateException("Cannot find 'client_config.properties'")
        }.onFailure { ex ->
            when (ex) {
                is IllegalArgumentException -> println("Configuration error: ${ex.message}")
                is IllegalStateException -> println("File error: ${ex.message}")
                else -> println("Failed to load configuration: ${ex.message}")
            }
        }

    }
}
