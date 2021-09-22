
import androidx.compose.runtime.MutableState


// coroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// robot
import java.awt.AWTException
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

var inputCommands = arrayListOf("UP", "LEFT", "RIGHT", "DOWN", "A", "B")
val playerCommands = HashMap<Int, HashMap<String, MutableState<String>>>() // [playerIndex: ["commandFromPhone":"commandToPress"]]

class Keyboard {
    private var active : Boolean = false
    private var sleep : Long = 10 // milliseconds
    private var robot : Robot = Robot()
    private var key : String = ""
    private var value : String = ""
    private var playerSlot : Int = -1

    //fun addKey(key : String, value : String) {
    fun addKey(key : String, value : String, playerSlot : Int) {
        this.key = key
        this.value = value
        this.playerSlot = playerSlot
    }

    fun enable() {
        if(!this.active){
            this.active = true

            // coroutine
            CoroutineScope(Dispatchers.IO).launch {
                keyPress()
            }
        }
    }

    fun disable() {
        this.active = false
    }

    private fun keyPress() {
        while(active) {
            try {

                //var keyEvent : Int = 0
                if (value.isNotEmpty() && playerSlot >= 0) {
                    /*when (value) {
                        "LEFT" -> keyEvent = KeyEvent.VK_LEFT
                        "UP" -> keyEvent = KeyEvent.VK_UP
                        "RIGHT" -> keyEvent = KeyEvent.VK_RIGHT
                        "DOWN" -> keyEvent = KeyEvent.VK_DOWN

                        "A" -> keyEvent = KeyEvent.VK_PAGE_UP
                        "B" -> keyEvent = KeyEvent.VK_SPACE
                    }*/
                    //var keyEvent = getKeyEvent(playerCommands[0]!![value]!!.value)
                    val keyEvent = getKeyEvent(playerCommands[playerSlot]!![value]!!.value)

                    robot.keyPress(keyEvent)
                    robot.keyRelease(keyEvent)
                    key = ""
                    value = ""
                    playerSlot = -1
                }

            } catch(e : AWTException){
                e.printStackTrace()
            }

            Thread.sleep(sleep)
        }
    }

    private fun getKeyEvent(letter : String) : Int {
        return when (letter.uppercase(Locale.getDefault())) {
            "0" -> KeyEvent.VK_0
            "1" -> KeyEvent.VK_1
            "2" -> KeyEvent.VK_2
            "3" -> KeyEvent.VK_3
            "4" -> KeyEvent.VK_4
            "5" -> KeyEvent.VK_5
            "6" -> KeyEvent.VK_6
            "7" -> KeyEvent.VK_7
            "8" -> KeyEvent.VK_8
            "9" -> KeyEvent.VK_9
            "A" -> KeyEvent.VK_A
            "B" -> KeyEvent.VK_B
            "C" -> KeyEvent.VK_C
            "D" -> KeyEvent.VK_D
            "E" -> KeyEvent.VK_E
            "F" -> KeyEvent.VK_F
            "G" -> KeyEvent.VK_G
            "H" -> KeyEvent.VK_H
            "I" -> KeyEvent.VK_I
            "J" -> KeyEvent.VK_J
            "K" -> KeyEvent.VK_K
            "L" -> KeyEvent.VK_L
            "M" -> KeyEvent.VK_M
            "N" -> KeyEvent.VK_N
            "O" -> KeyEvent.VK_O
            "P" -> KeyEvent.VK_P
            "Q" -> KeyEvent.VK_Q
            "R" -> KeyEvent.VK_R
            "S" -> KeyEvent.VK_S
            "T" -> KeyEvent.VK_T
            "U" -> KeyEvent.VK_U
            "V" -> KeyEvent.VK_V
            "W" -> KeyEvent.VK_W
            "X" -> KeyEvent.VK_X
            "Y" -> KeyEvent.VK_Y
            "Z" -> KeyEvent.VK_Z
            else -> {
                throw IOException("Keyboard.kt:getKeyEvent> key not found")
            }
        }
    }
}