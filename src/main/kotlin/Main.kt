import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.Navigator
import db.configureDatabases
import ui.NavTarget
import ui.main.NavHost
import utils.themes.AppTheme

fun main() = application {
    configureDatabases()

    Window(
        title = "Notariat",
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(placement = WindowPlacement.Maximized)
    ) {
        AppTheme(darkTheme = true) {
            Navigator(NavTarget.Main()) { navigator ->
                NavHost(navigator)
            }
        }
    }
}

