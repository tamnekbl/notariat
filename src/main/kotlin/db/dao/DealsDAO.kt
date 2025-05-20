package db.dao

import db.Deals
import db.DiscountsDeals
import db.ServicesDeals
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class DealDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DealDAO>(Deals)

    var date by Deals.date
    var commission by Deals.commission
    var description by Deals.description
    var client by ClientDAO.optionalReferencedOn(Deals.client)

    val discounts by DiscountDAO via DiscountsDeals
    val services by ServiceDAO via ServicesDeals

    fun toDeal() = Deal(
        id = id.value,
        date = date,
        commission = commission,
        description = description,
        clientId = client?.id?.value
    )
}

data class Deal(
    val id: Int,
    val date: String,
    val commission: Float,
    val description: String,
    val clientId: Int?,
    //val discountsId: Int?,
    //val servicesId: Int?
)

