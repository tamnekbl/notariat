package ui.discounts

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
            Toolbar(title = StringsRes.get("discounts"))
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
                listOf(it.id.toString(), it.name, it.discountAmount.toString(), it.description)
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