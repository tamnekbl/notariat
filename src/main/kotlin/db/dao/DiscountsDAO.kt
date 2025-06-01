package db.dao

import db.Discounts
import db.DiscountsDeals
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DiscountDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DiscountDAO>(Discounts)

    var name by Discounts.name
    var discountAmount by Discounts.discountAmount
    var description by Discounts.description

    val deals by DealDAO via DiscountsDeals

    fun toDiscount() = Discount(
        id = id.value,
        name = name,
        amount = discountAmount,
        description = description
    )
}

data class Discount(
    val id: Long,
    val name: String,
    val amount: Float,
    val description: String
)

data class DiscountWithDeals(
    val discount: Discount,
    val deals: List<SimpleDeal>
)