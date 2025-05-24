package ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
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

sealed class NavTarget(
    val name: String,
    val icon: ImageVector = Icons.Default.List,
    val route: String
) : Screen {
    class Deals : NavTarget("Deals", Icons.Default.Check, "deals") {
        @Composable
        override fun Content() {
            val model = rememberScreenModel { DealsModel(DealRepository()) }
            DealsView(model)
        }
    }
    class Clients : NavTarget("Clients", Icons.Default.Person, "clients") {
        @Composable
        override fun Content(){
            val model = rememberScreenModel { ClientsModel(ClientRepository()) }
            ClientsView(model)
        }
    }
    class Services : NavTarget("Services", Icons.Default.List, "services") {
        @Composable
        override fun Content() {
            val model = rememberScreenModel { ServicesModel(ServiceRepository()) }
            ServicesView(model)
        }
    }

    class Discounts : NavTarget("Discounts", Icons.Default.Add, "discounts") {
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

