package ui.deals

import db.dao.Deal
import ui.utils.ViewMode
import utils.Loading

data class DealsState(
    val loading: Loading = Loading.Idle,
    val viewMode: ViewMode = ViewMode.TABLE,
    val deal: Deal? = null,
    val currentDealIndex: Int = 0
)
