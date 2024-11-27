package io.github.octcarp.sustech.cs209a.linkgame.server.utils

import java.util.*

object ServerConfig {
    var serverPort: Int = 29903
        private set
    var reservedIds: List<String> = ArrayList()
        private set

    var playerRecordCsvPath: String = "/records/player_record.csv"
        private set
    var matchRecordCsvPath: String = "/records/match_record.csv"
        private set
    var maxClientNum: Int = 10
        private set


    init {
        runCatching {
            ServerConfig::class.java.getResourceAsStream("/server_config.properties")?.use { input ->
                Properties().apply {
                    load(input)
                    serverPort = getProperty("server.port", serverPort.toString()).toInt()
                    playerRecordCsvPath = getProperty("player_record_csv_path", playerRecordCsvPath)
                    matchRecordCsvPath = getProperty("match_record_csv_path", matchRecordCsvPath)
                    maxClientNum = getProperty("max_client_num", maxClientNum.toString()).toInt()

                    getProperty("reserved.ids")?.takeIf { it.isNotEmpty() }?.let { reservedIdsStr ->
                        reservedIds = reservedIdsStr.split(",").filter { it.isNotEmpty() }.map { it.trim() }
                    }
                }
            } ?: throw IllegalStateException("Cannot find 'server_config.properties'")
        }.onFailure { ex ->
            when (ex) {
                is IllegalArgumentException -> println("Configuration error: ${ex.message}")
                is IllegalStateException -> println("File error: ${ex.message}")
                else -> println("Failed to load configuration: ${ex.message}")
            }
        }
    }
}