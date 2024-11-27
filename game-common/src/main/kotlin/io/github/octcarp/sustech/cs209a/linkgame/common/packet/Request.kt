package io.github.octcarp.sustech.cs209a.linkgame.common.packet

import java.io.Serializable

data class Request(
    val type: RequestType,
    var data: Any? = null,
    var sender: String? = null,
) : Serializable
