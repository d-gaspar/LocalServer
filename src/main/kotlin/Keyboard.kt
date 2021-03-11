
// coroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// robot
import java.awt.AWTException
import java.awt.Robot
import java.awt.event.KeyEvent

// basic keyboard
data class BasicKeyboard (
    val key : String,
    val action : String
    //val vertical : Float,
    //val horizontal : Float
)

class Keyboard {
    private var active : Boolean = false
    private var sleep : Long = 10 // milliseconds
    private var robot : Robot = Robot()
    private var key : String = ""
    private var action : String = ""

    fun addKey(key : String, action : String) {
        this.key = key
        this.action = action
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

                var keyEvent : Int = 0
                if (key.isNotEmpty()) {
                    when (key) {
                        "LEFT" -> keyEvent = KeyEvent.VK_LEFT
                        "UP" -> keyEvent = KeyEvent.VK_UP
                        "RIGHT" -> keyEvent = KeyEvent.VK_RIGHT
                        "DOWN" -> keyEvent = KeyEvent.VK_DOWN

                        "A" -> keyEvent = KeyEvent.VK_PAGE_UP
                        "B" -> keyEvent = KeyEvent.VK_SPACE
                    }

                    robot.keyPress(keyEvent)
                    robot.keyRelease(keyEvent)
                    key = ""
                    action = ""

                    /*
                    // keyPress when DOWN, keyRelease when UP
                    when (action) {
                        "DOWN" -> robot.keyPress(keyEvent)
                        "UP" -> robot.keyRelease(keyEvent)
                    }
                    */
                }

            } catch(e : AWTException){
                e.printStackTrace()
            }

            Thread.sleep(sleep)
        }
    }
}