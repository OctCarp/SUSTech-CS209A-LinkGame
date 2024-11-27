package io.github.octcarp.sustech.cs209a.linkgame.server.net

import io.github.octcarp.sustech.cs209a.linkgame.common.model.MatchRecord
import io.github.octcarp.sustech.cs209a.linkgame.server.model.MatchInfo
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.FileIO
import java.time.LocalDateTime

object RecordManager {
    val matchRecordList: MutableList<MatchRecord> = FileIO.readMatchRecordList()

    fun addMatchRecordByInfo(matchInfo: MatchInfo): Boolean {
        val matchRecord = MatchRecord(
            p1 = matchInfo.p1,
            p2 = matchInfo.p2,
            p1Score = matchInfo.match.p1Score,
            p2Score = matchInfo.match.p2Score,
            endTime = LocalDateTime.now().toString(),
            result = matchInfo.result!!.toString()
        )

        matchRecordList.add(matchRecord)
        return FileIO.updateMatchRecordByList(matchRecordList)
    }
}
