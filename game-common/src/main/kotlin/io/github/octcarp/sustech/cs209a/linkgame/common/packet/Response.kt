package io.github.octcarp.sustech.cs209a.linkgame.common.packet

import java.io.Serializable

data class Response(
    val type: ResponseType,
    var data: Any? = null
) : Serializable
