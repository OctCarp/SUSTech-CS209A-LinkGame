package io.github.octcarp.sustech.cs209a.linkgame.common.packet

import java.io.Serializable

enum class SimpStatus(
    val code: Int,
    val message: String
) : Serializable {
    OK(200, "Request succeeded"),
    FAILURE(500, "Internal server error"),
    TIMEOUT(408, "Request timeout"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not found"),
    CONFLICT(409, "Conflict"),
    LOCKED(423, "Resource Locked")
}
