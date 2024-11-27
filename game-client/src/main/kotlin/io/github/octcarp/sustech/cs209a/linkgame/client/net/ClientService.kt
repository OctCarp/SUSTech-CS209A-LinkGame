package io.github.octcarp.sustech.cs209a.linkgame.client.net

import io.github.octcarp.sustech.cs209a.linkgame.client.utils.AlertPopper
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.ClientConfig
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.Request
import io.github.octcarp.sustech.cs209a.linkgame.common.packet.RequestType
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket
import java.util.concurrent.Executors

object ClientService {
    private var socket: Socket? = null
    private var ois: ObjectInputStream? = null
    private var oos: ObjectOutputStream? = null
    private var listener: ServerHandlerThread? = null
    private val executorService = Executors.newCachedThreadPool()

    var myId: String? = null

    init {
        runCatching {
            socket = Socket(InetAddress.getByName(ClientConfig.serverAddress), ClientConfig.serverPort)
            oos = ObjectOutputStream(socket!!.getOutputStream())
            ois = ObjectInputStream(socket!!.getInputStream())
            listener = ServerHandlerThread(socket!!, ois!!)
            Thread(listener).start()
        }.onFailure { ex ->
            when (ex) {
                is IllegalArgumentException -> println("Configuration error: ${ex.message}")
                is IllegalStateException -> println("File error: ${ex.message}")
                is IOException -> println("Failed to connect to server: ${ex.message}")
                else -> println("Failed to load configuration: ${ex.message}")
            }
        }
    }

    fun disconnect() {
        try {
            oos!!.writeObject(Request(RequestType.DISCONNECT))
            ois!!.close()
            oos!!.close()
            socket!!.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } finally {
            AlertPopper.popNetErrAndToLogin()
        }
    }

    fun sendRequest(request: Request) {
        executorService.submit {
            runCatching {
                if (request.sender == null) {
                    request.sender = if (myId == null) "default" else myId
                }
                oos!!.writeObject(request)
                oos!!.flush()
            }.onFailure {
                when (it) {
                    is IOException -> println("Failed to send request: ${it.message}")
                    else -> println("Failed to send request: ${it.message}")
                }
            }
        }
    }

    fun sendRequestOnlyType(type: RequestType) {
        sendRequest(Request(type))
    }
}