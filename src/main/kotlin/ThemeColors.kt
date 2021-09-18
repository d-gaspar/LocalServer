import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
//import java.awt.Color

// https://css-tricks.com/8-digit-hex-codes/ << 8 digit hexadecimal color

class ThemeColors {
    companion object {

        @Stable
        val light_grey = Color(0xFF353535)
        @Stable
        val dark_grey = Color(0xFF272727)
        @Stable
        val black = Color(0xFF121212)
        @Stable
        val white = Color(0xFFFFFFFF)

        @Stable
        val text = Color(0xFFd9d9d9)

        @Stable
        val buttonText = Color(0xFF272727)
        @Stable
        val buttonBackgroundOrange = Color(0xBFf16913)

        /*
        @Stable
        val player1 = Color(0xFFfb6a4a) // red
        @Stable
        val player2 = Color(0xFFfc8d62) // green
        @Stable
        val player3 = Color(0xFF8da0cb) // blue
        @Stable
        val player4 = Color(0xFFe78ac3) // yellow
        @Stable
        val player5 = Color(0xFFa6d854) // orange
        @Stable
        val player6 = Color(0xFFffd92f) // gray
        */


        @Stable
        val playerColorList = arrayListOf<Color>(
            Color(0xFFfb6a4a), // red
            Color(0xFF74c476), // green
            Color(0xFF43a2ca), // blue
            Color(0xFFffd92f), // yellow
            Color(0xFFfd8d3c), // orange
            Color(0xFF969696)  // gray
        )

        @Stable
        val playerColorHexList = arrayListOf<String>(
            "#fb6a4a", // red
            "#74c476", // green
            "#43a2ca", // blue
            "#ffd92f", // yellow
            "#fd8d3c", // orange
            "#969696"  // gray
        )
    }
}