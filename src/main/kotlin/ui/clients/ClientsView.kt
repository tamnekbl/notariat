package ui.clients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import utils.Size
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
                        onClick = {
                            model.onAction(Action.SetViewMode(ViewMode.SINGLE))
                            model.onAction(Action.LoadSingle())
                        }
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
                "PROFESSION" to 0.5f,
                "ADDRESS" to 0.5f,
                "PHONE NUMBER" to 0.5f,
            )
            val rows = model.clients.map {
                listOf(it.id.toString(), it.name, it.profession, it.address, it.phoneNumber)
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
                        ViewMode.SINGLE, ViewMode.EDIT -> ClientSingleView(model)
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
    if (state.clientFull == null) return

    SingleView {
        val scrollState = rememberScrollState()
        val clientToEdit = state.client
        val clientToShow = state.clientFull!!.client
        val deals = state.clientFull!!.deals

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Margin.mx)
        ) {
            TextRow(label = StringsRes.get("id"), value = clientToEdit.id.toString())

            if (state.viewMode.isEdit()) {
                EditableRow(
                    label = StringsRes.get("name"),
                    value = clientToEdit.name,
                    onValueChange = { model.updateClient(clientToEdit.copy(name = it)) })
                EditableRow(
                    label = StringsRes.get("profession"),
                    value = clientToEdit.profession,
                    onValueChange = { model.updateClient(clientToEdit.copy(profession = it)) })
                EditableRow(
                    label = StringsRes.get("address"),
                    value = clientToEdit.address,
                    onValueChange = { model.updateClient(clientToEdit.copy(address = it)) })
                EditableRow(
                    label = StringsRes.get("phone_number"),
                    value = clientToEdit.phoneNumber,
                    onValueChange = { model.updateClient(clientToEdit.copy(phoneNumber = it)) })
            } else {
                TextRow(label = StringsRes.get("name"), value = clientToShow.name)
                TextRow(label = StringsRes.get("profession"), value = clientToShow.profession)
                TextRow(label = StringsRes.get("address"), value = clientToShow.address)
                TextRow(label = StringsRes.get("phone_number"), value = clientToShow.phoneNumber)
            }

            Text(
                text = "${StringsRes.get("deals")}:",
                modifier = Modifier.width(Size.h),
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

        SingleViewButtons(
            isEditing = state.viewMode.isEdit(),
            nextPageEnabled = state.currentClientIndex < model.clients.size - 1 && !state.viewMode.isEdit(),
            prevPageEnabled = state.currentClientIndex > 0 && !state.viewMode.isEdit(),
        ) {
            model.onAction(it)
        }
    }
}


