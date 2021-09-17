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

val mapper = jacksonObjectMapper()

data class JsonData (
    val key : String,
    val value : String
)

class Server {
    private var server : ServerSocket? = null
    private var running : Boolean = false
    private var jsonString : String = ""
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
        var connected : Boolean = true

        reader = Scanner(client.getInputStream())
        writer = client.getOutputStream()

        // check password
        var jsonStringPass : String = ""
        try {
            jsonStringPass = reader?.nextLine().toString()

            if (jsonStringPass.isNotEmpty()) {
                var jsonPass : JsonData = mapper.readValue(jsonStringPass)

                if (jsonPass.key != "pass" || jsonPass.value != this.pass) {
                    connected = false
                    //writer?.write("Wrong password\n".toByteArray(Charset.defaultCharset()))
                    sendJson("{\"key\":\"pass\",\"value\":\"false\"}\n", client)
                }
            }
        } catch (e : Exception) {
            connected = false
            //e.printStackTrace()
        }

        //writer?.write("Welcome_to_server\n".toByteArray(Charset.defaultCharset()))
        if (connected) {
            sendJson("{\"key\":\"pass\",\"value\":\"true\"}\n", client)
        }

        while (connected) {
            println("OK-clientReaderHandler")

            try {
                jsonString = reader?.nextLine().toString()

                println("INPUT: $jsonString")
            } catch (e : Exception) {
                connected = false
                //e.printStackTrace()
            }

            Thread.sleep(1)
        }
    }

    /*******************************************************************************************/

    fun sendJson (json : String, client : Socket) {
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

            if (jsonString.isNotEmpty()) {
                val jsonData : JsonData = mapper.readValue(jsonString)

                if(jsonData.key.isEmpty()) {
                    keyboard.addKey("", "")
                } else {
                    keyboard.addKey(jsonData.key, jsonData.value)
                }

                jsonString = ""
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

    fun json() : String {
        return jsonString
    }
}