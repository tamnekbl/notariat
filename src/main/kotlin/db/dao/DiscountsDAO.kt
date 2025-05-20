package db.dao

import db.Discounts
import db.DiscountsDeals
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DiscountDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DiscountDAO>(Discounts)

    var name by Discounts.name
    var discountAmount by Discounts.discountAmount
    var description by Discounts.description

    val deals by DealDAO via DiscountsDeals

    fun toDiscount() = Discount(
        id = id.value,
        name = name,
        discountAmount = discountAmount,
        description = description
    )
}

data class Discount(
    val id: Int,
    val name: String,
    val discountAmount: Float,
    val description: String
)