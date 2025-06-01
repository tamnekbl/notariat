package ui.discounts

import db.dao.DiscountWithDeals
import ui.utils.ViewMode
import utils.Loading

data class DiscountsState(
    val loading: Loading = Loading.Idle,
    val viewMode: ViewMode = ViewMode.TABLE,
    val discount: DiscountWithDeals? = null,
    val currentDiscountIndex: Int = 0
)
