package ui.clients

import db.dao.ClientWithDeals
import ui.utils.ViewMode
import utils.Loading

data class ClientsState(
    val loading: Loading = Loading.Idle,
    val viewMode: ViewMode = ViewMode.TABLE,
    val client: ClientWithDeals? = null,
    val currentClientIndex: Int = 0
)



