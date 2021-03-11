// desktop compose
import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// json - jackson
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.awt.AWTException

// coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// server socket
import java.net.ServerSocket

class main {
    private var aaa : Int = 0

    constructor(aaa : Int){
        this.aaa = aaa
        println("AAAAA")
    }
}

fun main(){

    // ktor - netty
    //var server = NettyServer()

    // server socket
    val server = Server()

    // desktop compose
    println("START WINDOW")
    val windowSize = mutableStateOf(IntSize.Zero)
    val serverStatus = mutableStateOf("off")
    val jsonString = mutableStateOf("")
    Window (
        title = "Local Server 0.1",
        events = WindowEvents(
            onResize = {
                size -> windowSize.value = size
            },
            onClose = {
                println("WINDOW CLOSE")
                //server.stop() // nettyServer
                println("WINDOW CLOSED")
            },
            onFocusGet = {
                println("FOCUS GET")
                server.keyboardEnable(false)
            },
            onFocusLost = {
                println("FOCUS LOST")
                server.keyboardEnable(true)
            }
        )
    ) {
        // content
        Box(
            modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            ) {
                //Text(text = "Size: ${windowSize.value}")

                Text(text = "Server status: ${serverStatus.value} (${jsonString.value})")
                Spacer(modifier = Modifier.width(10.dp).height(10.dp))

                Button("Start server", {
                    println("START SERVER")
                    server.run()
                    serverStatus.value = "on"
                })

                Button("Stop server", {
                    println("STOP SERVER")
                    server.shutdown()
                    serverStatus.value = "off"
                })

                Button("Refresh jsonString", {
                    jsonString.value = server.json()
                })

            }
        }
    }

    println("END FILE")
}

@Composable
fun Button(
    text : String = "",
    action : (() -> Unit)? = null,
    width : Dp = 150.dp,
    height : Dp = 50.dp
) {
    Button(
        modifier = Modifier.size(width, height),
        onClick = { action?.invoke() }
    ) {
        Text(text)
    }

    // add margin
    Spacer(modifier = Modifier.width(10.dp).height(10.dp))
}