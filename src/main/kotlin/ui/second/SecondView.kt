package ui.second

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun SecondView() {
    val navigator = LocalNavigator.currentOrThrow


    DisposableEffect(Unit){
        onDispose {  }
    }

    Column {
        Text("Это второй экран")
        Button(onClick = { navigator.pop() }) {
            Text("Назад")
        }
    }
}
