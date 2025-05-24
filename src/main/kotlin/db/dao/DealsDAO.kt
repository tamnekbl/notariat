package db.dao

import db.Deals
import db.DiscountsDeals
import db.ServicesDeals
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ui.NavTarget
import java.time.LocalDate

class DealDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DealDAO>(Deals)

    var date by Deals.date
    var commission by Deals.commission
    var description by Deals.description
    var clientId by Deals.clientId

    val client by ClientDAO referencedOn Deals.clientId
    val discounts by DiscountDAO via DiscountsDeals
    val services by ServiceDAO via ServicesDeals

    fun toSimpleDeal() = SimpleDeal(
        id = id.value,
        date = date,
        commission = commission,
        description = description,
        clientId = clientId.value
    )

    fun toDeal() = Deal(
        id = id.value,
        date = date,
        commission = commission,
        description = description,
        client = client.toClient(),
        services = services.map { it.toService() },
        discounts = discounts.map { it.toDiscount() }
    )
}

data class SimpleDeal(
    val id: Long,
    val date: LocalDate,
    val commission: Float,
    val description: String,
    val clientId: Long,
)

data class Deal(
    val id: Long,
    val date: LocalDate,
    val commission: Float,
    val description: String,
    val client: Client,
    val services: List<Service>,
    val discounts: List<Discount>
)

