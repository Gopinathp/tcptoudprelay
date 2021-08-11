package client

import server.TCP_SERVER_PORT
import java.lang.Thread.sleep
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread

fun main() {
    val socket = Socket("localhost",TCP_SERVER_PORT)
    thread {
        val reader = socket.getInputStream().bufferedReader()
        while (true) {
            val line = reader.readLine()
            if (line != null) {
                println("received: $line")
            } else {
                break
            }
        }
        println("End of recv thread")
    }

    val turnOffDsPacket = "{\"r\":\"fu8y5zHZSieWa4bc64HXGT:APA91bEc3FQJgAZgra339TiWWqofa822-Uu5a7ZU9cd_xn8wxX4Z6Ao0zjDVYP_omSF2p_7HaRG2sGHAD3U1K12Q_Cf8t6NmQqh7le7cm_fgYhva2W9J7TC7BQHJ-ocl7j6a-9gNv77i\",\"src\":49890,\"d\":\"bVHYStCg46NbKzfWlVGwH0hlzkKdHy7gJg\\/6QZVGEldED67prQ9zYQ77LkBONu4qJvKfOHmlX3G6\\nGT6nDWuyYm5jKAOsAqkZGobr3e2uQmWOIZFUtPMSbyRVkKBjzrw3h1RqdJJHE666JnHpl7ygVrGe\\n8SlxYGJl\\/IpmkbWVPrC5hfewztOap0d74luyMbc6mj3KsB3PQygbJNJqTzelL5jw1E1wUcYB\\/VJN\\ngkxmQBFD+iEs\\/H6vZSd60rtHC9BbqQueQhad+pw1f+\\/gBfnAkrLrYvzjdAGNaQRyBMwz8oZdF9La\\nl3aDIAYv1n23KbmC8mBs\\/2ZCxqEb1fhdJfsozg==\\n\"}"
    val writer = socket.getOutputStream().bufferedWriter()
    while (true) {
        try {
            writer.write(turnOffDsPacket)
            writer.flush()
            writer.newLine()
            writer.flush()
            println("sent turnOffDsPacket")
            sleep(3000)
        } catch (e: SocketException) {
            break
        }
    }
    println("End of Client simulation")
}