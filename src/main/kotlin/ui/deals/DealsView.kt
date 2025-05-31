package ui.deals

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
import ui.utils.Table
import ui.utils.Toolbar
import utils.Loading
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
            Toolbar(title = StringsRes.get("deals"))
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

            if (state.loading is Loading.Success)
                Table(headers, rows) {}
            else
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
        }
    }
}

