package io.github.octcarp.sustech.cs209a.linkgame.client.net

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MatchRecordController
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher
import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType

object RecordData {
    private var playerList: MutableList<String> = mutableListOf()
    private var playerMatchRecordMap: MutableMap<String, MutableList<MatchRecord>> = mutableMapOf()

    fun recordRequest() = ClientService.sendRequestOnlyType(RequestType.GET_MATCH_RECORD)

    fun reSyncRecord(records: MutableList<MatchRecord>) {
        playerMatchRecordMap.clear()
        playerList.clear()

        records.forEach { record ->
            listOf(record.p1, record.p2).forEach { player ->
                if (player !in playerList) {
                    playerList.add(player)
                }

                playerMatchRecordMap.getOrPut(player) { mutableListOf() }.add(record)
            }
        }

        (SceneSwitcher.getController("match-record") as MatchRecordController).updateRecordPlayerList(playerList)
    }

    fun getRecordByPlayer(player: String): MutableList<MatchRecord>? {
        return playerMatchRecordMap[player]
    }

}
