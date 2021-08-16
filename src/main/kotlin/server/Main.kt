package server

import java.lang.IllegalStateException
import kotlin.concurrent.thread

val TCP_SERVER_PORT = System.getenv("TCP_SERVER_PORT")?.toInt() ?: 10001
val UPSTREAM_UDP_ADDRESS = System.getenv("UPSTREAM_UDP_ADDRESS") ?: throw IllegalStateException("Upstream address undefined")
val UPSTREAM_UDP_PORT = System.getenv("UPSTREAM_UDP_PORT")?.toInt() ?: throw IllegalStateException("Upstream port undefined")


fun main() {
    TcpServer.listen()
    Runtime.getRuntime().addShutdownHook(thread {
        println("Shutting Down")
    })
    println("End of program")
}