package ui.services

import db.dao.Service
import db.dao.ServiceWithDeals
import ui.utils.ViewMode
import utils.Loading

data class ServicesState(
    val loading: Loading = Loading.Idle,
    val viewMode: ViewMode = ViewMode.TABLE,
    val serviceFull: ServiceWithDeals? = null,
    val service: Service = Service(),
    val currentServiceIndex: Int = 0
)
