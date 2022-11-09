package server

import java.io.IOException
import java.net.ServerSocket
import kotlin.system.exitProcess

object TcpServer {

    private lateinit var serverSocket: ServerSocket

    init {
        createServerSocket()
    }

    private fun createServerSocket() {
        try {
            serverSocket = ServerSocket(TCP_SERVER_PORT)
            println("Listening on port $TCP_SERVER_PORT")
        } catch (e: IOException) {
            println("Failed to open the specified socket: $TCP_SERVER_PORT")
            exitProcess(-1)
        }
    }

    fun listen() {
        while (true) {
            val socket = serverSocket.accept()
            socket.keepAlive = true
            println("Received a new socket ${socket.port} ${socket.inetAddress.hostAddress}")
            TcpSocketHandler(socket)
        }
    }
}