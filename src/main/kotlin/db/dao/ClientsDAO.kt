package db.dao

import db.Clients
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class ClientDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClientDAO>(Clients)

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
    val id: Int,
    val name: String,
    val profession: String,
    val address: String,
    val phoneNumber: String
)