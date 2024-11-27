package io.github.octcarp.sustech.cs209a.linkgame.server.utils

import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Player
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.concurrent.CopyOnWriteArrayList

object FileIO {

    private val playerCSVFormat = CSVFormat.DEFAULT.builder().setHeader("ID", "Password").build()
    private val recordCSVFormat = CSVFormat.DEFAULT.builder()
        .setHeader("p1", "p2", "p1_score", "p2_score", "end_time", "result").build()

    private val playerListFile by lazy {
        FileIO::class.java.getResource(ServerConfig.playerRecordCsvPath)
            ?.let { File(it.toURI()) }
            ?: error("Cannot find player record CSV file")
    }

    private val matchRecordFile by lazy {
        FileIO::class.java.getResource(ServerConfig.matchRecordCsvPath)
            ?.let { File(it.toURI()) }
            ?: error("Cannot find match record CSV file")
    }

    fun readPlayerList(): MutableList<Player> {
        val playerList = CopyOnWriteArrayList<Player>()
        CSVFormat.Builder.create(CSVFormat.DEFAULT).setSkipHeaderRecord(true)
            .build().parse(playerListFile.reader())
            .drop(1)
            .map {
                Player(
                    id = it[0],
                    password = it[1]
                )
            }.also(playerList::addAll)
        return playerList
    }

    fun readMatchRecordList(): MutableList<MatchRecord> {
        val matchRecordList = CopyOnWriteArrayList<MatchRecord>()
        CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setSkipHeaderRecord(true)
        }.build().parse(matchRecordFile.reader())
            .drop(1)
            .map {
                MatchRecord(
                    p1 = it[0],
                    p2 = it[1],
                    p1Score = it[2].toInt(),
                    p2Score = it[3].toInt(),
                    endTime = it[4],
                    result = it[5]
                )
            }.also(matchRecordList::addAll)
        return matchRecordList
    }

    fun updatePlayerByList(playerList: List<Player>): Boolean {
        return try {
            Thread {
                BufferedWriter(FileWriter(playerListFile)).use { writer ->
                    val csvPrinter = CSVPrinter(writer, playerCSVFormat)
                    playerList.forEach { player ->
                        csvPrinter.printRecord(player.id, player.password)
                    }
                    csvPrinter.flush()
                }
            }.start()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun updateMatchRecordByList(matchRecordList: List<MatchRecord>): Boolean {
        return try {
            Thread {
                BufferedWriter(FileWriter(matchRecordFile)).use { writer ->
                    val csvPrinter = CSVPrinter(writer, recordCSVFormat)
                    matchRecordList.forEach { record ->
                        csvPrinter.printRecord(
                            record.p1, record.p2,
                            record.p1Score, record.p2Score,
                            record.endTime, record.result.toString()
                        )
                    }
                    csvPrinter.flush()
                }
            }.start()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

