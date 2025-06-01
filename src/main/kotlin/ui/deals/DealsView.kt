package ui.deals

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
import utils.logging.Log
import utils.res.StringsRes

@Composable
fun DealsView(model: DealsModel) {

    val state by model.state.collectAsState()

    DisposableEffect(Unit) {
        model.getDeals()
        Log.i(model.toString())
        onDispose {  }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = StringsRes.get("deals")
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
                "DATE" to 0.3f,
                "COMMISSION" to 0.2f,
                "DESCRIPTION" to 0.5f,
                "CLIENT ID" to 0.1f
            )
            val rows = model.simpleDeals.map {
                listOf(it.id.toString(), it.date.toString(), it.commission.toString(), it.description, it.clientId.toString())
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

                        ViewMode.SINGLE -> DealSingleView(model)
                        ViewMode.EDIT -> TODO()
                    }
                }

                else -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun DealSingleView(
    model: DealsModel,
) {
    val state by model.state.collectAsState()
    if (state.deal == null)
        return

    SingleView {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Margin.mx)
        ) {
            val deal = state.deal!!
            val client = state.deal!!.client
            val services = state.deal!!.services
            val discounts = state.deal!!.discounts
            TextRow(label = StringsRes.get("id"), value = deal.id.toString())
            TextRow(label = StringsRes.get("date"), value = deal.date.toString())
            TextRow(label = StringsRes.get("commission"), value = deal.commission.toString())
            TextRow(label = StringsRes.get("description"), value = deal.description)
            Text(
                text = "${StringsRes.get("client")}:",
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
                    data = listOf(client)
                )
            }
            Text(
                text = "${StringsRes.get("services")}:",
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
                    data = services
                )
            }

            Text(
                text = "${StringsRes.get("discounts")}:",
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
                    data = discounts
                )
            }
        }
        PageController(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            canNext = state.currentDealIndex < model.simpleDeals.size - 1,
            canPrev = state.currentDealIndex > 0
        ) {
            model.onAction(Action.PrevNext(it))
        }
    }
}

