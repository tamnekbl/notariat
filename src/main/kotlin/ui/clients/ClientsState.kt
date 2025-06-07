package ui.clients

import db.dao.Client
import db.dao.ClientWithDeals
import ui.utils.ViewMode
import utils.Loading

data class ClientsState(
    val loading: Loading = Loading.Idle,
    val viewMode: ViewMode = ViewMode.TABLE,
    val clientFull: ClientWithDeals? = null,
    val client: Client = Client(),
    val currentClientIndex: Int = 0
)



