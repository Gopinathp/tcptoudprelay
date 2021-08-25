package server

import logStacktrace
import java.net.*
import java.nio.charset.Charset
import kotlin.concurrent.thread


class TcpSocketHandler(private val tcpSocket: Socket) {

    private lateinit var tcpInputProcessingThread: Thread
    private lateinit var udpInputProcessingThread: Thread
    private lateinit var upstreamSocket: DatagramSocket

    init {
        initUpstreadUdpServerSocket()
        processSocket()
    }

    private fun initUpstreadUdpServerSocket() {
        upstreamSocket = DatagramSocket()
        this.udpInputProcessingThread = thread {
            try {
                val d = ByteArray(30 * 1024)
                val p = DatagramPacket(d, d.size)
                val writer = tcpSocket.getOutputStream().bufferedWriter()
                while (true) {
                    upstreamSocket.receive(p)
                    val bytes: ByteArray = d.copyOfRange(0, p.length)
                    val msg = String(bytes, Charset.forName("ISO-8859-1"))
                    println("Upstream sent $msg from $UPSTREAM_UDP_ADDRESS:$UPSTREAM_UDP_PORT")
                    writer.write(msg)
                    writer.newLine()
                    writer.flush()
                    println("Sent to Tcp Client")
                }
            } catch (e: SocketException) {
                terminateRelaySession()
            }
        }
    }

    private fun terminateRelaySession() {
        println("terminatingRelaySession")
        logStacktrace()
        if (!upstreamSocket.isClosed) {
            println("Closing upstream UDP socket")
            upstreamSocket.close()
        }
        if (!tcpSocket.isClosed) {
            println("Closing the TCP socket")
            tcpSocket.close()
        }
        if (!udpInputProcessingThread.isInterrupted) {
            println("Stopping UDP input stream processing")
            udpInputProcessingThread.interrupt()
        }
        if (!tcpInputProcessingThread.isInterrupted) {
            println("Stopping TCP input stream processing")
            tcpInputProcessingThread.interrupt()
        }
    }

    private fun processSocket() {
        this.tcpInputProcessingThread = thread {
            try {
                val reader = tcpSocket.getInputStream().bufferedReader()
                while (true) {
                    val readLine = reader.readLine()
                    if (readLine == null) {
                        terminateRelaySession()
                        break
                    }
                    println("received $readLine.")
                    if (readLine.trim().isNotEmpty()) {
                        println("Sending to UDP Upstream")
                        val bytes = readLine.toByteArray()
                        val p = DatagramPacket(bytes, bytes.size)
                        p.socketAddress = InetSocketAddress(UPSTREAM_UDP_ADDRESS, UPSTREAM_UDP_PORT)
                        upstreamSocket.send(p)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                terminateRelaySession()
            }
        }
    }
}