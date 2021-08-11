package server

import kotlin.concurrent.thread

val TCP_SERVER_PORT = System.getenv("TCP_SERVER_PORT")?.toInt() ?: 10001
val UPSTREAM_UDP_ADDRESS = System.getenv("UPSTREAM_UDP_ADDRESS") ?: "staging.myeglu.com"
val UPSTREAM_UDP_PORT = System.getenv("UPSTREAM_UDP_PORT")?.toInt() ?: 8015


fun main() {
    TcpServer.listen()
    Runtime.getRuntime().addShutdownHook(thread {
        println("Shutting Down")
    })
    println("End of program")
}