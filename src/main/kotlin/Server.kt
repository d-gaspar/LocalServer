import androidx.compose.ui.graphics.toArgb
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

var playerSlotsAvailable = arrayListOf<Boolean>(true, true, true, true, true, true )

val mapper = jacksonObjectMapper()

data class JsonData (
    val key : String,
    val value : String
)

class Server {
    private var server : ServerSocket? = null
    private var running : Boolean = false
    //private var jsonString : String = ""
    private var jsonString : Pair<String, Int> = Pair("", -1)
    public var port : Int = 8080
    public var pass : String = "123456"

    var reader : Scanner? = null
    var writer : OutputStream? = null

    // keyboard
    val keyboard : Keyboard = Keyboard()

    /*******************************************************************************************/

    fun run(pass : String = "123456") {
        // return if server is already running
        if (running) return

        server = ServerSocket(port)
        this.pass = pass
        println("Server if running (port: ${server!!.localPort}, pass: $pass)")
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
                    println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU1")
                    clientReaderHandler(client)
                    println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU2")
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
        var connected : Boolean = true

        reader = Scanner(client.getInputStream())
        writer = client.getOutputStream()

        var slotIndex : Int = -1

        // check password
        var jsonStringPass : String = ""
        try {
            jsonStringPass = reader?.nextLine().toString()

            if (jsonStringPass.isNotEmpty()) {
                var jsonPass : JsonData = mapper.readValue(jsonStringPass)

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
        var slotAvailable : Boolean = false
        for (slot in 0 until playerSlotsAvailable.size) {
            if (playerSlotsAvailable[slot]) {
                playerSlotsAvailable[slot] = false
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

        while (connected) {
            println("OK-clientReaderHandler")

            try {
                //jsonString = reader?.nextLine().toString()
                jsonString = Pair(reader?.nextLine().toString(), slotIndex)

                println("INPUT: $jsonString")
            } catch (e : Exception) {
                connected = false
                //e.printStackTrace()
            }

            Thread.sleep(1)
        }

        // disconnect player
        playerSlotsAvailable[slotIndex] = true
    }

    /*******************************************************************************************/

    private fun sendJson (json : String, client : Socket) {
        //CoroutineScope(Dispatchers.IO).launch {
        try {
            println("sendJson: $json")
            writer = client.getOutputStream()
            val input = readLine() ?: ""
            writer?.write(json.toByteArray(Charset.defaultCharset()))
            writer?.flush()
            //writer?.close()

        } catch (e : IOException) {
            e.printStackTrace()
        }
        //}
    }

    /*******************************************************************************************/

    private fun executeCommands() {
        while(running) {

            //if (jsonString.isNotEmpty()) {
                //val jsonData : JsonData = mapper.readValue(jsonString)
            if (jsonString.first.isNotEmpty() && jsonString.second >= 0) {
                val jsonData : JsonData = mapper.readValue(jsonString.first)

                if(jsonData.key.isEmpty()) {
                    //keyboard.addKey("", "")
                    keyboard.addKey("", "", -1)
                } else {
                    //keyboard.addKey(jsonData.key, jsonData.value)
                    keyboard.addKey(jsonData.key, jsonData.value, jsonString.second)
                }

                //jsonString = ""
                jsonString = Pair("", -1)
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