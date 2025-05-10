package db

import androidx.compose.ui.graphics.CloseSegment
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

// Таблица клиентов
object Clients : IntIdTable("clients") {
    val name = text("name")
    val profession = text("profession")
    val address = text("address")
    val phoneNumber = text("phone_number")
}

class ClientDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClientDAO>(Clients)

    val name by Clients.name
    val profession by Clients.profession
    val address by Clients.address
    val phoneNumber by Clients.phoneNumber

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



// Сделки
object Deals : IntIdTable("deals") {
    val date = text("date")
    val commission = float("commission")
    val description = text("description")
    val client = reference("client_id", Clients).nullable()
}

// Скидки
object Discounts : IntIdTable("discounts") {
    val name = text("name")
    val discountAmount = float("discount_amount")
    val description = text("description")
}

// Таблица связей скидок и сделок
object DiscountsDeals : Table("discounts_deals") {
    val discount = reference("discount_id", Discounts)
    val deal = reference("deal_id", Deals)
    override val primaryKey = PrimaryKey(discount, deal)
}

// Услуги
object Services : IntIdTable("services") {
    val name = text("name")
    val description = text("description")
    val price = float("price").nullable()
}

// Таблица связей услуг и сделок
object ServicesDeals : Table("services_deals") {
    val service = reference("service_id", Services)
    val deal = reference("deal_id", Deals)
    override val primaryKey = PrimaryKey(service, deal)
}