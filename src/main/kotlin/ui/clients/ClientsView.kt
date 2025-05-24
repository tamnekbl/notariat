package ui.clients

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import ui.utils.Toolbar
import utils.Loading
import utils.logging.Log
import utils.res.StringsRes

@Composable
fun ClientsView(model: ClientsModel) {

    val state by model.state.collectAsState()

    DisposableEffect(Unit) {
        model.getClients()
        Log.i(model.toString())
        onDispose {  }
    }

    Scaffold(
        topBar = {
            Toolbar(title = StringsRes.get("clients"))
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            //contentAlignment = Alignment.Center
        ) {
            val headers = mapOf(
                "ID" to 0.1f,
                "NAME" to 0.5f,
                "ADDRESS" to 0.5f,
                "PHONE NUMBER" to 0.5f,
            )
            val rows = model.clients.map {
                listOf(it.id.toString(), it.name, it.address, it.phoneNumber)
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleTable(headers: Map<String, Float>, rows: List<List<String>>) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Заголовки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.onPrimary),
        ) {
            headers.forEach { (header, weight) ->
                Text(
                    text = header,
                    modifier = Modifier
                        .weight(weight)
                        .padding(8.dp),
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.width(96.dp))
        }

        // Данные
        LazyColumn {
            itemsIndexed(rows) { index, row ->
                var isHovered by remember { mutableStateOf(false) }
                val backgroundColor = if (index % 2 == 0)
                    MaterialTheme.colors.onSecondary.copy(alpha = 0.5f)
                else
                    MaterialTheme.colors.onSecondary

                Row(
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .pointerMoveFilter(
                            onEnter = {
                                isHovered = true
                                false
                            },
                            onExit = {
                                isHovered = false
                                false
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEachIndexed { index, cell ->
                        val weight = headers.values.toList()[index]
                        Text(
                            text = cell,
                            modifier = Modifier
                                .weight(weight)
                                .padding(8.dp)
                        )
                    }

                    AnimatedVisibility(visible = !isHovered){
                        Spacer(modifier = Modifier.width(96.dp)) // резервируем место, чтобы не прыгало
                    }
                    AnimatedVisibility(visible = isHovered){
                        Row {
                            IconButton(
                                onClick = {  }
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }

                            IconButton(
                                onClick = { showDeleteDialog = true }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    }

                }
            }
        }
    }


/*    if (showDeleteDialog){
        Dialog1(
            title = "Delete",
            onDismiss = {
            showDeleteDialog = false
        }){

            Text("Every day")
        }
    }*/
}