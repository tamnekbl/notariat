package db.reps

import db.dao.Service
import db.dao.ServiceDAO
import db.suspendTransaction

class ServiceRepository {

    suspend fun create(service: Service): Service = suspendTransaction {
        ServiceDAO.new {
            name = service.name
            description = service.description
            price = service.price
        }.toService()
    }

    suspend fun getById(id: Int): Service? = suspendTransaction {
        ServiceDAO.findById(id)?.toService()
    }

    suspend fun getAll(): List<Service> = suspendTransaction {
        ServiceDAO.all().map { it.toService() }
    }

    suspend fun update(id: Int, updated: Service): Boolean = suspendTransaction {
        ServiceDAO.findByIdAndUpdate(id) { service ->
            service.apply {
                name = updated.name
                description = updated.description
                price = updated.price
            }
        } != null
    }

    suspend fun delete(id: Int): Boolean = suspendTransaction {
        ServiceDAO.findById(id)?.delete() != null
    }
}