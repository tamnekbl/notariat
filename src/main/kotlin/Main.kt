import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.Navigator
import db.configureDatabases
import ui.NavTarget
import ui.main.NavHost
import utils.res.StringsRes
import utils.themes.AppTheme

fun main() = application {
    configureDatabases()

    Window(
        title = StringsRes.get("app_name"),
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

