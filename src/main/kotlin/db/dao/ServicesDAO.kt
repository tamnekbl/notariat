package db.dao

import db.Services
import db.ServicesDeals
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ServiceDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ServiceDAO>(Services)

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
    val id: Int,
    val name: String,
    val description: String,
    val price: Float
)