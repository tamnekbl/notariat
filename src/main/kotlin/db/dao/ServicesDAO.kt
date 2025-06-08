package db.dao

import db.Services
import db.ServicesDeals
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ServiceDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ServiceDAO>(Services)

    var name by Services.name
    var description by Services.description
    var price by Services.price

    val deals by DealDAO via ServicesDeals

    fun toService() = Service(
        id = id.value,
        name = name,
        description = description,
        price = price
    )
}

data class Service(
    val id: Long = -1,
    val name: String = "",
    val description: String = "",
    val price: Float = 0f
)

data class ServiceWithDeals(
    val service: Service,
    val deals: List<SimpleDeal>
)