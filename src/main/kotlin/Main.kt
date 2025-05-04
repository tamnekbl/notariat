import androidx.compose.material.Scaffold
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import ui.NavTarget
import utils.themes.AppTheme

fun main() = application {
    Window(
        title = "Notariat",
        onCloseRequest = ::exitApplication
    ) {
        AppTheme(darkTheme = true) {
            Scaffold {
                Navigator(NavTarget.Main())
            }
        }
    }
}

