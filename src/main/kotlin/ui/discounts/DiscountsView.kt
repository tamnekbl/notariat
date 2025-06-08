package ui.discounts

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
import utils.res.StringsRes

@Composable
fun DiscountsView(model: DiscountsModel) {

    val state by model.state.collectAsState()

    DisposableEffect(Unit) {
        model.getDiscounts()
        onDispose {  }
    }

    Scaffold(
        topBar = {
            Toolbar(
                title = StringsRes.get("discounts")
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
            val headers = mapOf(
                "ID" to 0.1f,
                "NAME" to 0.3f,
                "AMOUNT" to 0.2f,
                "DESCRIPTION" to 0.5f,
            )
            val rows = model.discounts.map {
                listOf(it.id.toString(), it.name, it.amount.toString(), it.description)
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

                        ViewMode.SINGLE, ViewMode.EDIT -> DiscountSingleView(model)
                    }
                }

                else -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun DiscountSingleView(
    model: DiscountsModel,
) {
    val state by model.state.collectAsState()
    if (state.discountFull == null)
        return

    SingleView {
        val scrollState = rememberScrollState()
        val discountToShow = state.discountFull!!.discount
        val deals = state.discountFull!!.deals
        val discountToEdit = state.discount

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Margin.mx)
        ) {
            TextRow(label = StringsRes.get("id"), value = discountToShow.id.toString())

            if (state.viewMode.isEdit()) {
                EditableRow(
                    label = StringsRes.get("name"),
                    value = discountToEdit.name,
                    onValueChange = { model.updateDiscount(discountToEdit.copy(name = it)) })
                EditableRow(
                    label = StringsRes.get("amount"),
                    value = discountToEdit.amount,
                    onValueChange = { model.updateDiscount(discountToEdit.copy(amount = it.coerceIn(0f, 1f))) })
                EditableRow(
                    label = StringsRes.get("description"),
                    value = discountToEdit.description,
                    onValueChange = { model.updateDiscount(discountToEdit.copy(description = it)) })
            } else {
                TextRow(label = StringsRes.get("name"), value = discountToShow.name)
                TextRow(label = StringsRes.get("amount"), value = discountToShow.amount.toString())
                TextRow(label = StringsRes.get("description"), value = discountToShow.description)
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
            nextPageEnabled = state.currentDiscountIndex < model.discounts.size - 1 && !state.viewMode.isEdit(),
            prevPageEnabled = state.currentDiscountIndex > 0 && !state.viewMode.isEdit(),
        ) {
            model.onAction(it)
        }
    }
}