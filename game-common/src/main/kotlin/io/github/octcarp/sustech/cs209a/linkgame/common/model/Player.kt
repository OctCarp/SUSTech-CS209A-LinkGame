package io.github.octcarp.sustech.cs209a.linkgame.common.model

import java.io.Serializable

data class Player(
    val id: String,
    val password: String
) : Serializable {
    init {
        require(!id.isBlank()) { "id cannot be null or empty" }
        require(!password.isBlank()) { "password cannot be null or empty" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Player) return false
        return id == other.id && password == other.password
    }

    override fun hashCode(): Int = id.hashCode()
}
