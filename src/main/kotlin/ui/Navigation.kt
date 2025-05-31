package ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import db.reps.ClientRepository
import db.reps.DealRepository
import db.reps.DiscountRepository
import db.reps.ServiceRepository
import ui.clients.ClientsModel
import ui.clients.ClientsView
import ui.deals.DealsModel
import ui.deals.DealsView
import ui.discounts.DiscountsModel
import ui.discounts.DiscountsView
import ui.services.ServicesModel
import ui.services.ServicesView
import ui.utils.EmptyView
import utils.res.StringsRes

sealed class NavTarget(
    val name: String,
    val icon: ImageVector = Icons.Default.List,
    val route: String
) : Screen {
    class Deals : NavTarget(StringsRes.get("deals"), Icons.Default.Check, "deals") {
        @Composable
        override fun Content() {
            val model = rememberScreenModel { DealsModel(DealRepository()) }
            DealsView(model)
        }
    }

    class Clients : NavTarget(StringsRes.get("clients"), Icons.Default.Person, "clients") {
        @Composable
        override fun Content(){
            val model = rememberScreenModel { ClientsModel(ClientRepository(DealRepository())) }
            ClientsView(model)
        }
    }

    class Services : NavTarget(StringsRes.get("services"), Icons.Default.List, "services") {
        @Composable
        override fun Content() {
            val model = rememberScreenModel { ServicesModel(ServiceRepository()) }
            ServicesView(model)
        }
    }

    class Discounts : NavTarget(StringsRes.get("discounts"), Icons.Default.Add, "discounts") {
        @Composable
        override fun Content() {
            val model = rememberScreenModel { DiscountsModel(DiscountRepository()) }
            DiscountsView(model)
        }
    }

    class Main : NavTarget("Main", Icons.Default.Menu,"main") {
        @Composable
        override fun Content() = EmptyView()
    }

    companion object {
        val menu_items = listOf(
            Deals(),
            Clients(),
            Services(),
            Discounts(),
            Main()
        )
    }

}

