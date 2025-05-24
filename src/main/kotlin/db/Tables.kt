package db


import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date


// Таблица клиентов
object Clients : LongIdTable("clients") {
    val name = varchar("name", 256)
    val profession = varchar("profession", 256)
    val address = varchar("address", 512)
    val phoneNumber = varchar("phone_number", 32)
}

// Сделки
object Deals : LongIdTable("deals") {
    val date = date("date")
    val commission = float("commission")
    val description = text("description")
    val clientId = reference("client_id", Clients)
}

// Скидки
object Discounts : LongIdTable("discounts") {
    val name = varchar("name", 128)
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
object Services : LongIdTable("services") {
    val name = varchar("name", 128)
    val description = text("description")
    val price = float("price")
}

// Таблица связей услуг и сделок
object ServicesDeals : Table("services_deals") {
    val service = reference("service_id", Services)
    val deal = reference("deal_id", Deals)
    override val primaryKey = PrimaryKey(service, deal)
}