// desktop compose
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.WindowEvents
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.Window
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

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
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import java.net.InetAddress

// server socket
import java.net.ServerSocket

fun main() = application {

    // server socket
    val server = Server()
    var pass by remember { mutableStateOf("123456") }

    println("START WINDOW")
    val windowState = rememberWindowState()
    //val windowSize = mutableStateOf(IntSize.Zero)
    val serverStatus = mutableStateOf("off")
    //val jsonString = mutableStateOf("")
    Window (
        state = windowState,
        onCloseRequest = { println("close"); exitApplication() },//::exitApplication,
        title = "Local Server 0.1"
    ) {
        /** resize event */
        /*LaunchedEffect(windowState) {
            snapshotFlow { windowState.size }
                .onEach { /*println("resize")*/ }
                .launchIn(this)
        }*/

        /** focus event */
        // PENDENT
        DisposableEffect(Unit) {
            window.addWindowFocusListener(object : WindowFocusListener {
                override fun windowGainedFocus(e: WindowEvent) {
                    println("FOCUS GAINED")
                    server.keyboardEnable(false)
                }

                override fun windowLostFocus(e: WindowEvent) {
                    println("FOCUS LOST")
                    server.keyboardEnable(true)
                }
            })
            onDispose {}
        }

        /***************************************************************************************/
        /***************************************************************************************/
        /***************************************************************************************/
        /***************************************************************************************/
        /***************************************************************************************/

        /** CONTENT */
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(ThemeColors.light_grey)
        ) {
            /***********************************************************************************/
            /** TOP MENU */
            /***********************************************************************************/

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
                        text(
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
                    button(
                        if (serverStatus.value == "off") "Start" else "Stop",
                        {
                            if (serverStatus.value == "off") {
                                println("START")
                                server.run()
                                serverStatus.value = "on"
                            } else {
                                println("STOP")
                                server.shutdown()
                                serverStatus.value = "off"
                            }
                        }
                    )

                    Column (
                    ) {
                        text("IP: ${InetAddress.getLocalHost().hostAddress}")

                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            text("Pass: ", addMargin = false)

                            BasicTextField(
                                value = pass,
                                onValueChange = { pass = it },
                                modifier = Modifier
                                    .background(Color.Black)
                                    .width(50.dp)
                                    .padding(2.dp)
                                    ,
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = ThemeColors.text,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }

            /***********************************************************************************/

            /** PLAYERS */

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ThemeColors.light_grey)
            ) {
                players("PLAYER 1")
                players("PLAYER 2")
                players("PLAYER 3")
                players("PLAYER 4")
                players("PLAYER 5")
                players("PLAYER 6")
            }

            /***********************************************************************************/

            /*
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
            }*/
        }
    }
}

/***********************************************************************************************/

@Composable
fun text (
    text : String = "",
    bold : Boolean = false,
    fontSize : TextUnit = TextUnit.Unspecified,
    addMargin : Boolean = true
) {
    Text(
        text = text,
        color = ThemeColors.text,
        fontSize = fontSize,
        fontWeight = FontWeight(
            if(bold) 700 else 400
        )
    )

    // add margin
    if(addMargin) Spacer(modifier = Modifier.width(10.dp).height(10.dp))
}

@Composable
fun button (
    text : String = "",
    action : (() -> Unit)? = null,
    width : Dp = 150.dp,
    height : Dp = 50.dp
) {
    Button(
        modifier = Modifier.size(width, height),
        onClick = { action?.invoke() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ThemeColors.buttonBackgroundOrange,
            contentColor = ThemeColors.buttonText
        )
    ) {
        Text(text, fontSize = 20.sp,fontWeight = FontWeight(700))
    }

    // add margin
    Spacer(modifier = Modifier.width(10.dp).height(10.dp))
}

@Composable
fun RowScope.players (
    title : String = "PLAYER X"
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(ThemeColors.black)
            .weight(1f)
            .border(2.dp, Color.DarkGray)
            .padding(5.dp)
    ) {
        // title
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            text(title)

            /** circle */
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if ("off" == "off") Color.Red else Color.Green)
            )
        }

    }
}

/*
@Composable
fun Circle(color: Color,
           modifier: Modifier = Modifier) {
    Box(
            modifier = modifier.composed {
                //Modifier.preferredSize(32.dp)
                Modifier.defaultMinSize(32.dp)
                        .clip(CircleShape)
                        .background(color)
            }
    )
}*/