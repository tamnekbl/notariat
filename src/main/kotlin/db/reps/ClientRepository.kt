package db.reps

import db.dao.Client
import db.dao.ClientDAO
import db.suspendTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ClientRepository {

    suspend fun create(client: Client): Client = suspendTransaction {
        ClientDAO.new {
            name = client.name
            profession = client.profession
            address = client.address
            phoneNumber = client.phoneNumber
        }.toClient()
    }

    suspend fun getById(id: Long): Client? = suspendTransaction {
        ClientDAO.findById(id)?.toClient()
    }

    suspend fun getAll(): List<Client> = suspendTransaction {
        ClientDAO.all().map { it.toClient() }
    }

    suspend fun update(id: Long, updated: Client): Boolean = suspendTransaction {
        ClientDAO.findByIdAndUpdate(id){
            it.name = updated.name
            it.profession = updated.profession
            it.address = updated.address
            it.phoneNumber = updated.phoneNumber
        } != null
    }

    suspend fun delete(id: Long): Boolean = suspendTransaction {
        ClientDAO.findById(id)?.delete() != null
    }
}