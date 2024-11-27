package io.github.octcarp.sustech.cs209a.linkgame.server

import io.github.octcarp.sustech.cs209a.linkgame.server.net.ClientHandlerThread
import io.github.octcarp.sustech.cs209a.linkgame.server.utils.ServerConfig
import java.io.IOException
import java.net.ServerSocket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


fun main(args: Array<String>) {
    val server = Server()

    server.startServer()
}

class Server {
    private val port: Int

    var threadPool: ExecutorService? = null

    constructor() {
        this.port = ServerConfig.serverPort
    }

    constructor(port: Int) {
        this.port = port
    }

    fun initServer() {
        threadPool = Executors.newCachedThreadPool()
    }

    fun startServer() {
        initServer()
        try {
            ServerSocket(port).use { serverSocket ->
                println("Server started on port $port")
                while (true) {
                    val socket = serverSocket.accept()
                    println("Client Connect:" + socket.getRemoteSocketAddress())
                    threadPool!!.execute(ClientHandlerThread(socket))
                }
            }
        } catch (ex: IOException) {
            throw RuntimeException(ex)
        }
    }
}