package ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import ui.main.MainView
import ui.second.SecondView

sealed class NavTarget : Screen {
    class Main : NavTarget() {
        @Composable
        override fun Content() = MainView()
    }
    class Second : NavTarget() {
        @Composable
        override fun Content() = SecondView()
    }
}