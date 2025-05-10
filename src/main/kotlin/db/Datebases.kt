package db


import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun configureDatabases() {
    val dbUrl = "jdbc:postgresql://127.0.0.1:5432/notariat"
    val dbUser = "postgres"
    val dbPassword = "admin"

    Database.connect(
        dbUrl,
        user = dbUser,
        password = dbPassword
    )
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
