package ui.clients

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.utils.*
import utils.Loading
import utils.Margin
import utils.logging.Log
import utils.res.StringsRes

@Composable
fun ClientsView(model: ClientsModel) {

    val state by model.state.collectAsState()

    DisposableEffect(Unit) {
        model.getClients()
        Log.i(model.toString())
        onDispose { }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = StringsRes.get("clients"),
            ) {
                if (state.viewMode == ViewMode.TABLE)
                    IconButton(
                        onClick = { model.onAction(Action.SingleView()) }
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }
                else
                    IconButton(
                        onClick = { model.onAction(Action.TableView()) }
                    ) {
                        Icon(Icons.Default.List, contentDescription = null)
                    }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            //contentAlignment = Alignment.Center
        ) {
            //todo file
            val headers = mapOf(
                "ID" to 0.1f,
                "NAME" to 0.5f,
                "ADDRESS" to 0.5f,
                "PHONE NUMBER" to 0.5f,
            )
            val rows = model.clients.map {
                listOf(it.id.toString(), it.name, it.address, it.phoneNumber)
            }

            when (state.loading) {
                is Loading.Success -> {
                    when (state.viewMode) {
                        ViewMode.TABLE -> Table(
                            headers = headers,
                            rows = rows,
                            onAction = {
                                model.onAction(it)
                            }
                        )

                        ViewMode.SINGLE -> ClientSingleView(model)
                        ViewMode.EDIT -> TODO()
                    }
                }

                else -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}


@Composable
fun ClientSingleView(
    model: ClientsModel,
) {
    val state by model.state.collectAsState()
    if (state.client == null)
        return

    SingleView {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val client = state.client!!.client
            val deals = state.client!!.deals
            TextRow(label = StringsRes.get("id"), value = client.id.toString())
            TextRow(label = StringsRes.get("name"), value = client.name)
            TextRow(label = StringsRes.get("address"), value = client.address)
            TextRow(label = StringsRes.get("phone_number"), value = client.phoneNumber)
            Text(
                text = "${StringsRes.get("deals")}:",
                modifier = Modifier.width(120.dp),
                style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primary)
            )
            Card(
                backgroundColor = MaterialTheme.colors.background.copy(0.5f),
                elevation = 0.dp
            ) {
                DataClassTable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Margin.m),
                    data = deals
                )
            }
        }
        PageController(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            onPrev = { model.onAction(Action.Prev()) },
            onNext = { model.onAction(Action.Next()) },
            canNext = state.currentClientIndex < model.clients.size - 1,
            canPrev = state.currentClientIndex > 0
        )
    }
}

@Composable
fun TextRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "$label:",
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primary)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1
        )
    }
}

