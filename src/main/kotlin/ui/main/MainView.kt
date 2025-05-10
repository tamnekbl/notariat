package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import ui.NavTarget
import ui.NavTarget.Companion.menu_items
import utils.logging.Log



@Composable
fun NavHost(navigator: Navigator) {
    DisposableEffect(Unit) {
        onDispose {
            Log.d("Dispose")
        }
    }

    val navBarItems = menu_items.take(4)

    Row {
        NavigationView(
            navBarItems = navBarItems,
            current = navigator.lastItem as NavTarget?
        ) {
            navigator.push(it)
        }

        Box(
            modifier = Modifier.weight(1f),
        ) {
            CurrentScreen()
        }
    }

}

@Composable
fun NavigationView(
    modifier: Modifier = Modifier,
    navBarItems: List<NavTarget>,
    current: NavTarget?,
    onItemClick: (NavTarget) -> Unit
){
    NavigationRail(
        modifier = Modifier
            .then(modifier),
        backgroundColor = MaterialTheme.colors.background
    ){
        navBarItems.forEach { item ->
            val isCurrent = current?.route == item.route
            NavigationRailItem(
                selected = isCurrent,
                onClick = { onItemClick(item) },
                icon = { Icon(item.icon, item.route) },
                label = {
                    Text(
                        text = item.name
                    )
                }
            )
        }
    }
}