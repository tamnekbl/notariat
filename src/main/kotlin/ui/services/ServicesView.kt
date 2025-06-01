package ui.services

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
import ui.clients.TextRow
import ui.utils.*
import utils.Loading
import utils.Margin
import utils.Size
import utils.res.StringsRes

@Composable
fun ServicesView(model: ServicesModel) {

    val state by model.state.collectAsState()

    DisposableEffect(Unit) {
        model.getServices()
        onDispose {  }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = StringsRes.get("services")
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
            val headers = mapOf(
                "ID" to 0.1f,
                "NAME" to 0.3f,
                "PRICE" to 0.2f,
                "DESCRIPTION" to 0.5f,
            )
            val rows = model.services.map {
                listOf(it.id.toString(), it.name, it.price.toString(), it.description)
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

                        ViewMode.SINGLE -> ServiceSingleView(model)
                        ViewMode.EDIT -> TODO()
                    }
                }

                else -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun ServiceSingleView(
    model: ServicesModel,
) {
    val state by model.state.collectAsState()
    if (state.service == null)
        return

    SingleView {
        val scrollState = rememberScrollState()

        /*        move to box
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.End)
                .fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState)
        )*/
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Margin.mx)
        ) {
            val service = state.service!!.service
            val deals = state.service!!.deals
            TextRow(label = StringsRes.get("id"), value = service.id.toString())
            TextRow(label = StringsRes.get("name"), value = service.name)
            TextRow(label = StringsRes.get("price"), value = service.price.toString())
            TextRow(label = StringsRes.get("description"), value = service.description)
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

        PageController(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            canNext = state.currentServiceIndex < model.services.size - 1,
            canPrev = state.currentServiceIndex > 0
        ) {
            model.onAction(Action.PrevNext(it))
        }
    }
}