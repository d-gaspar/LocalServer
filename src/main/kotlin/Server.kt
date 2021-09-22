import androidx.compose.runtime.mutableStateOf
import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset
import java.util.*

var playerSlotsAvailable = hashMapOf(0 to mutableStateOf(true), 1 to mutableStateOf(true), 2 to mutableStateOf(true), 3 to mutableStateOf(true), 4 to mutableStateOf(true), 5 to mutableStateOf(true))

data class JsonData (
    val key : String,
    val value : String
)

val mapper : ObjectMapper = jacksonObjectMapper()

class Server {
    private var server : ServerSocket? = null
    private var running : Boolean = false
    private var queue : LinkedList<Pair<String, Int>> = LinkedList() // [command, playerIndex]
    private var port : Int = 8080
    private var pass : String = "123456"

    private var writer : OutputStream? = null

    // keyboard
    private val keyboard : Keyboard = Keyboard()

    /*******************************************************************************************/

    fun run(pass : String = "123456") {


        // return if server is already running
        if (running) return

        server = ServerSocket(port)
        this.pass = pass
        println("Server is running (port: ${server!!.localPort}, pass: $pass)")
        running = true

        // read inputs
        CoroutineScope(Dispatchers.IO).launch {
            clientHandler()
        }

        // execute commands
        CoroutineScope(Dispatchers.IO).launch {
            executeCommands()
        }
    }

    /*******************************************************************************************/

    fun shutdown() {
        running = false

        server?.close()
    }

    /*******************************************************************************************/

    private fun clientHandler() {

        while(running) {
            try {
                val client = server!!.accept()
                println("Client connected: ${client?.inetAddress?.hostAddress}")

                CoroutineScope(Dispatchers.IO).launch {
                    clientReaderHandler(client)
                }

                println("OK-clientHandler")
            } catch (e : SocketException) {
                //e.printStackTrace()
                println("SOCKET CLOSED")
            }

            //Thread.sleep(1000)
        }
    }

    /*******************************************************************************************/

    private fun clientReaderHandler(client : Socket) {
        var connected = true

        val reader = Scanner(client.getInputStream())
        writer = client.getOutputStream()
        //reader = Scanner(client.getInputStream())
        //writer = client.getOutputStream()

        var slotIndex : Int = -1

        // check password
        val jsonStringPass: String
        try {
            jsonStringPass = reader.nextLine().toString()

            if (jsonStringPass.isNotEmpty()) {
                val jsonPass = mapper.readValue<JsonData>(jsonStringPass)

                if (jsonPass.key != "pass" || jsonPass.value != this.pass) {
                    connected = false

                    // wrong_pass -> value:"false"
                    sendJson("{\"key\":\"pass\",\"value\":\"false\"}\n", client)
                }
            }
        } catch (e : Exception) {
            connected = false
            //e.printStackTrace()
        }

        // correct password
        if (connected) {
            // wrong_pass -> value:"true"
            sendJson("{\"key\":\"pass\",\"value\":\"true\"}\n", client)
        }

        // send playerColor ("null" if there isn't player slot available)
        var slotAvailable = false
        for (slot in 0 until playerSlotsAvailable.size) {
            if (playerSlotsAvailable[slot]!!.value) {
                playerSlotsAvailable[slot]!!.value = false
                slotAvailable = true
                slotIndex = slot
                break
            }
        }

        if (slotAvailable) {
            sendJson("{\"key\":\"playerColor\",\"value\":\"${ThemeColors.playerColorHexList[slotIndex]}\"}\n", client) // player slot 1
        } else {
            sendJson("{\"key\":\"playerColor\",\"value\":\"null\"}\n", client) // player slot unavailable
        }

        //var t : String? = null
        while (connected) {
            println("OK-clientReaderHandler")

            try {
                queue.add(Pair(reader.nextLine().toString(), slotIndex))

                println("INPUT: ${queue.last}")
            } catch (e: Exception) {
                connected = false
                e.printStackTrace()
            }

            Thread.sleep(1)
        }

        // disconnect player
        playerSlotsAvailable[slotIndex]!!.value = true
    }

    /*******************************************************************************************/

    private fun sendJson (json : String, client : Socket) {
        try {
            println("sendJson: $json")
            writer = client.getOutputStream()
            //val input = readLine() ?: ""
            writer?.write(json.toByteArray(Charset.defaultCharset()))
            writer?.flush()
            //writer?.close()

        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

    /*******************************************************************************************/

    private fun executeCommands() {
        while(running) {

            if (queue.size>0 && queue.first.first.isNotEmpty() && queue.first.second >= 0) {
                val jsonData : JsonData = mapper.readValue(queue.first.first)

                if(jsonData.key.isEmpty()) {
                    keyboard.addKey("", "", -1)
                } else {
                    keyboard.addKey(jsonData.key, jsonData.value, queue.first.second)
                }

                queue.remove()
            }

            Thread.sleep(1)
        }
    }

    /*******************************************************************************************/

    fun keyboardEnable(enable : Boolean = true) {
        if (enable) {
            keyboard.enable()
        } else {
            keyboard.disable()
        }
    }

    /*******************************************************************************************/

    /*fun json() : String {
        return jsonString
    }*/
}