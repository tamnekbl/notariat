package db.dao

import db.Clients
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class ClientDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ClientDAO>(Clients)

    var name by Clients.name
    var profession by Clients.profession
    var address by Clients.address
    var phoneNumber by Clients.phoneNumber

    fun toClient() = Client(
        id = id.value,
        name = name,
        profession = profession,
        address = address,
        phoneNumber = phoneNumber
    )
}

data class Client(
    val id: Long,
    val name: String,
    val profession: String,
    val address: String,
    val phoneNumber: String
)

data class ClientWithDeals(
    val client: Client,
    val deals: List<SimpleDeal>
)