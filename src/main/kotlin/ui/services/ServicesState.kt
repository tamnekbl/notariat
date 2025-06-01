package ui.services

import db.dao.ServiceWithDeals
import ui.utils.ViewMode
import utils.Loading

data class ServicesState(
    val loading: Loading = Loading.Idle,
    val viewMode: ViewMode = ViewMode.TABLE,
    val service: ServiceWithDeals? = null,
    val currentServiceIndex: Int = 0
)
