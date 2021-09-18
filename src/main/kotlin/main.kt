/**
 * author: https://github.com/d-gaspar
 *
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.Window
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase

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
import kotlinx.coroutines.withContext
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import java.io.IOException
import java.net.InetAddress

// server socket
import java.net.ServerSocket
import java.util.*

fun main() = application {

    // server socket
    val server = Server()
    var pass by remember { mutableStateOf("123456") }
    var passLength : Int = 6

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
                    .padding(start = 10.dp, top = 40.dp, end = 10.dp, bottom = 10.dp)
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
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    button(
                        if (serverStatus.value == "off") "Start" else "Stop",
                        {
                            if (serverStatus.value == "off") {
                                println("START")
                                server.run(pass)
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

                            BasicTextField (
                                value = pass,
                                onValueChange = {
                                    if (it.length <= passLength && it.matches("^[a-zA-Z0-9]*$".toRegex())) {
                                        pass = it
                                    }
                                },
                                enabled = serverStatus.value=="off",
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = ThemeColors.text,
                                    textAlign = TextAlign.Left
                                ),
                                modifier = Modifier
                                    .background(if (serverStatus.value=="off") Color.Black else Color.Transparent)
                                    .width(60.dp)
                                    .padding(2.dp)
                            )
                        }
                    }
                }

                text("https://github.com/d-gaspar", addMargin = false)
            }

            /***********************************************************************************/

            /** PLAYERS */

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ThemeColors.light_grey)
            ) {
                players(0, "WADSVB".toCharArray())
                players(1, "5132OP".toCharArray())
                players(2)
                players(3)
                players(4)
                players(5)
            }
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

/***********************************************************************************************/

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

/***********************************************************************************************/

@Composable
fun RowScope.players (
    playerIndex : Int = 0,
    defaultKeys : CharArray = "WADSVB".toCharArray(),
    playerColor : Color = if (playerIndex < ThemeColors.playerColorList.size) ThemeColors.playerColorList[playerIndex] else Color.Black
) {
    if (defaultKeys.size != 6) throw IOException("main.kt:playersFunction> defaultKeys must have size=6")

    // top key
    var keyUp by remember { mutableStateOf(defaultKeys[0].toString()) }

    // middle key
    var keyLeft by remember { mutableStateOf(defaultKeys[1].toString()) }
    var keyRight by remember { mutableStateOf(defaultKeys[2].toString()) }

    // bottom key
    var keyDown by remember { mutableStateOf(defaultKeys[3].toString()) }

    // right keys
    var keyA by remember { mutableStateOf(defaultKeys[4].toString()) }
    var keyB by remember { mutableStateOf(defaultKeys[5].toString()) }

    // add keys to Keyboard class (playerCommands)
    if (playerIndex !in playerCommands.keys) {
        playerCommands[playerIndex] = hashMapOf()
        for (i in 0 until 6) {
            playerCommands[playerIndex]!![inputCommands[i]] = mutableStateOf(
                if (defaultKeys.size > i) defaultKeys[i].toString() else ""
            )
        }
    }

    var modifierTextFieldColumn : Modifier = Modifier
        .weight(1f)
        .fillMaxSize()
        .background(Color.DarkGray)

    var modifierTextField : Modifier = Modifier
        .padding(2.dp)

    var styleTextField : TextStyle = TextStyle (
        color = ThemeColors.text,
        textAlign = TextAlign.Center
    )

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Black)
            .weight(1f)
            .border(2.dp, Color.DarkGray)
            .padding(5.dp)
            .height(150.dp)
    ) {
        /** title */
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            text("PLAYER ${playerIndex+1}")

            // circle
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if ("off" == "off") Color.Red else Color.Green)
            )
        }

        // margin between title and body of player
        Spacer(modifier = Modifier.height(10.dp))

        /***************************************************************************************/
        /** keys */
        Row (
            modifier = Modifier.background(playerColor).border(2.dp, playerColor)
        ) {
            /** left keys */
            Column (
                modifier = Modifier
                    .weight(3f),
                verticalArrangement = Arrangement.Center
            ) {
                /** top */
                Row (
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("", modifier = Modifier.weight(1f))

                    Column (
                        modifier = modifierTextFieldColumn,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BasicTextField (
                            value = keyUp,
                            onValueChange = {
                                keyUp = keyValueChange(keyUp, it)
                                playerCommands[playerIndex]!!["UP"]!!.value = keyUp
                            },
                            textStyle = styleTextField,
                            modifier = modifierTextField
                        )
                    }

                    Text("", modifier = Modifier.weight(1f))
                }

                /** middle */
                Row (modifier = Modifier.weight(1f)) {
                    Column (
                        modifier = modifierTextFieldColumn,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BasicTextField (
                            value = keyLeft,
                            onValueChange = {
                                keyLeft = keyValueChange(keyLeft, it)
                                playerCommands[playerIndex]!!["LEFT"]!!.value = keyLeft
                            },
                            textStyle = styleTextField,
                            modifier = modifierTextField
                        )
                    }

                    Text("", modifier = Modifier.weight(1f))

                    Column (
                        modifier = modifierTextFieldColumn,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BasicTextField (
                            value = keyRight,
                            onValueChange = {
                                keyRight = keyValueChange(keyRight, it)
                                playerCommands[playerIndex]!!["RIGHT"]!!.value = keyRight
                            },
                            textStyle = styleTextField,
                            modifier = modifierTextField
                        )
                    }
                }

                /** bottom */
                Row (modifier = Modifier.weight(1f)) {
                    Text("", modifier = Modifier.weight(1f))

                    Column (
                        modifier = modifierTextFieldColumn,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BasicTextField (
                            value = keyDown,
                            onValueChange = {
                                keyDown = keyValueChange(keyDown, it)
                                playerCommands[playerIndex]!!["DOWN"]!!.value = keyDown
                            },
                            textStyle = styleTextField,
                            modifier = modifierTextField
                        )
                    }

                    Text("", modifier = Modifier.weight(1f))
                }
            }

            /***********************************************************************************/
            /** right keys */
            Column (
                modifier = Modifier
                    .weight(2f)
            ) {
                /** top */
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text("", modifier = Modifier.weight(1f))

                    Column (
                        modifier = modifierTextFieldColumn,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BasicTextField (
                            value = keyA,
                            onValueChange = {
                                keyA = keyValueChange(keyA, it)
                                playerCommands[playerIndex]!!["A"]!!.value = keyA
                            },
                            textStyle = styleTextField,
                            modifier = modifierTextField
                        )
                    }
                }

                /** middle */
                Row (modifier = Modifier.weight(1f)) {}

                /** bottom */
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text("", modifier = Modifier.weight(1f))

                    Column (
                        modifier = modifierTextFieldColumn,
                        verticalArrangement = Arrangement.Center
                    ) {
                        BasicTextField (
                            value = keyB,
                            onValueChange = {
                                keyB = keyValueChange(keyB, it)
                                playerCommands[playerIndex]!!["B"]!!.value = keyB
                            },
                            textStyle = styleTextField,
                            modifier = modifierTextField
                        )
                    }
                }
            }
        }
    }
}

fun keyValueChange (
    key : String = "",
    newKey : String = ""
) : String {
    var aux = newKey.replace(key, "").lowercase(Locale.getDefault())

    aux = Regex("[^a-z0-9]").replace(aux, "")

    if (aux.isNotEmpty() && aux.matches("^[a-z0-9]*$".toRegex())) {
        return aux.last().toString().uppercase(Locale.getDefault())
    } else if (!aux.matches("^[a-z0-9]*$".toRegex())) {
        return ""
    }

    return key.uppercase(Locale.getDefault())
}

/***********************************************************************************************/

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