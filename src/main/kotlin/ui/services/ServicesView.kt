package ui.services

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.clients.SimpleTable
import ui.utils.Toolbar
import utils.Loading

@Composable
fun ServicesView(model: ServicesModel) {

    val state by model.state.collectAsState()

    DisposableEffect(Unit) {
        model.getServices()
        onDispose {  }
    }

    Scaffold(
        topBar = {
            Toolbar(title = "Services")
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            //contentAlignment = Alignment.Center
        ) {
            val headers = mapOf(
                "ID" to 0.1f,
                "NAME" to 0.3f,
                "PRICE" to 0.2f,
                "DESCRIPTION" to 0.5f,
            )
            val rows = model.services.map {
                listOf(it.id.toString(), it.name, it.price.toString(), it.description)
            }

            if (state.loading is Loading.Success)
                SimpleTable(headers, rows)
            else
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
        }
    }
}