package ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import ui.utils.EmptyView

sealed class NavTarget(
    val name: String,
    val icon: ImageVector = Icons.Default.List,
    val route: String
) : Screen {
    class Deals : NavTarget("Deals", Icons.Default.Check, "deals") {
        @Composable
        override fun Content() = EmptyView()
    }
    class Clients : NavTarget("Clients", Icons.Default.Person, "clients") {
        @Composable
        override fun Content() = EmptyView()
    }
    class Services : NavTarget("Services", Icons.Default.List, "services") {
        @Composable
        override fun Content() = EmptyView()
    }

    class Discounts : NavTarget("Discounts", Icons.Default.Add, "discounts") {
        @Composable
        override fun Content() = EmptyView()
    }

    class Main : NavTarget("Main", Icons.Default.Menu,"main") {
        @Composable
        override fun Content() = EmptyView()
    }

    companion object {
        val menu_items = listOf(
            Deals(),
            Clients(),
            Services(),
            Discounts(),
            Main()
        )
    }

}

