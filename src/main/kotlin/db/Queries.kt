package db

class Queries {
    suspend fun getAll(): List<Client> = suspendTransaction {
        ClientDAO.all().map{ it.toClient() }
    }
}