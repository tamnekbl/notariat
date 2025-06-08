package ui.discounts

import db.dao.Discount
import db.dao.DiscountWithDeals
import ui.utils.ViewMode
import utils.Loading

data class DiscountsState(
    val loading: Loading = Loading.Idle,
    val viewMode: ViewMode = ViewMode.TABLE,
    val discountFull: DiscountWithDeals? = null,
    val discount: Discount = Discount(),
    val currentDiscountIndex: Int = 0
)
