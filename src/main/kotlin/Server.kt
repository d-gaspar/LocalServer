import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.nio.charset.Charset
import java.util.*

val mapper = jacksonObjectMapper()

class Server {
    private var server : ServerSocket? = null
    private var running : Boolean = false
    private var jsonString : String = ""
    public var port : Int = 8080

    var reader : Scanner? = null
    var writer : OutputStream? = null

    // keyboard
    val keyboard : Keyboard = Keyboard()

    /*******************************************************************************************/

    fun run() {
        // return if server is already running
        if (running) return

        server = ServerSocket(port)
        println("Server if running (port: ${server!!.localPort})")
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

        while (connected) {
            println("OK-clientReaderHandler")

            writer = client.getOutputStream()
            writer?.write("Welcome_to_server\n".toByteArray(Charset.defaultCharset()))

            reader = Scanner(client.getInputStream())
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

    private fun executeCommands() {
        while(running) {

            if (jsonString.isNotEmpty()) {
                val basicKeyboard : BasicKeyboard = mapper.readValue(jsonString)

                if(basicKeyboard.key.isEmpty()) {
                    keyboard.addKey("", "")
                } else {
                    keyboard.addKey(basicKeyboard.key, basicKeyboard.action)
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