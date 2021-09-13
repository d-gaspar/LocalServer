// desktop compose
import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

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
import java.net.InetAddress

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

    // server socket
    //val server = Server()
/*
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
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            /***********************************************************************************/
            /** TOP MENU */

            Column (
                modifier = Modifier
                    .background(ThemeColors.dark_grey)
                    .padding(start = 10.dp, top = 50.dp, end = 10.dp, bottom = 10.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /** main title */
                        Text(
                            text = "Server",
                            bold = true,
                            fontSize = 35.sp
                        )

                        /** circle */
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(if (serverStatus.value == "off") Color.Red else Color.Green)
                        )
                    }
                }

                /** IP */
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("IP: ${InetAddress.getLocalHost().hostAddress}")
                }
            }

            /***********************************************************************************/

            /** BODY */
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ThemeColors.light_grey),
                verticalAlignment = Alignment.CenterVertically
            ) {

                /** LEFT MENU */
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp)
                        //.align(Alignment.CenterEnd)
                ) {

                    Button(
                        if (serverStatus.value == "off") "Start server" else "Stop server",
                    {
                        if (serverStatus.value == "off") {
                            println("START SERVER")
                            server.run()
                            serverStatus.value = "on"
                        } else {
                            println("STOP SERVER")
                            server.shutdown()
                            serverStatus.value = "off"
                        }
                    })

                    Text("USERS")
                    Text("CONTROLS")
                    Text("LOG")
                    Text("TAB 1")
                    Text("TAB 2")
                    Text("TAB 3")

                    Text("https://github.com/d-gaspar")
                }

                /*******************************************************************************/

                /** RIGHT BODY */
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        //.fillMaxHeight()
                        .background(ThemeColors.black)
                        .padding(10.dp)
                ) {
                    //Text(text = "Size: ${windowSize.value}")

                    /*Button(
                        "Refresh jsonString",
                        { jsonString.value = server.json() },
                        height = 70.dp
                    )*/
                }
            }
        }
    }

    println("END FILE")*/
}

/***********************************************************************************************/

@Composable
fun Text(
    text : String = "",
    bold : Boolean = false,
    fontSize : TextUnit = TextUnit.Unspecified
) {
    /*Text(
        text = text,
        color = ThemeColors.text,
        fontSize = fontSize,
        fontWeight = FontWeight(
            if(bold) 700 else 400
        )
    )*/

    // add margin
    //Spacer(modifier = Modifier.width(10.dp).height(10.dp))
}

@Composable
fun Button(
    text : String = "",
    action : (() -> Unit)? = null,
    width : Dp = 150.dp,
    height : Dp = 50.dp
) {
    /*Button(
        modifier = Modifier.size(width, height),
        onClick = { action?.invoke() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ThemeColors.buttonBackgroundOrange,
            contentColor = ThemeColors.buttonText
        )
    ) {
        Text(text)
    }*/

    // add margin
    //Spacer(modifier = Modifier.width(10.dp).height(10.dp))
}
/*
@Composable
fun Circle(color: Color,
           modifier: Modifier = Modifier) {
    Box(
            modifier = modifier.composed {
                Modifier.preferredSize(32.dp)
                        .clip(CircleShape)
                        .background(color)
            }
    )
}*/