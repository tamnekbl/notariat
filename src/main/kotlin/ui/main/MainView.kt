package ui.main

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.rememberNavigatorScreenModel
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ui.NavTarget
import utils.logging.Log



@Composable
fun MainView() {
    val navigator = LocalNavigator.currentOrThrow
    var text by remember { mutableStateOf("Hello, World!") }
    val model = navigator.rememberNavigatorScreenModel { MainModel() }


    DisposableEffect(Unit) {
        Log.e(model.toString())

        model.start()
        onDispose {
            Log.i("Dispose")
            model.onDispose()
        }
    }


    Button(onClick = { navigator.push(NavTarget.Second()) }) {
        Text("Перейти ко второму экрану")

    }
}