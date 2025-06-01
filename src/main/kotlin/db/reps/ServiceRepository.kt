package db.reps

import db.dao.Service
import db.dao.ServiceDAO
import db.dao.ServiceWithDeals
import db.suspendTransaction

class ServiceRepository {

    suspend fun create(service: Service): Service = suspendTransaction {
        ServiceDAO.new {
            name = service.name
            description = service.description
            price = service.price
        }.toService()
    }

    suspend fun getWithDeals(id: Long): ServiceWithDeals? = suspendTransaction {
        val serviceDAO = ServiceDAO.findById(id) ?: return@suspendTransaction null

        ServiceWithDeals(
            service = serviceDAO.toService(),
            deals = serviceDAO.deals.map { it.toSimpleDeal() }
        )
    }

    suspend fun getById(id: Long): Service? = suspendTransaction {
        ServiceDAO.findById(id)?.toService()
    }

    suspend fun getAll(): List<Service> = suspendTransaction {
        ServiceDAO.all().map { it.toService() }
    }

    suspend fun update(id: Long, updated: Service): Boolean = suspendTransaction {
        ServiceDAO.findByIdAndUpdate(id) { service ->
            service.apply {
                name = updated.name
                description = updated.description
                price = updated.price
            }
        } != null
    }

    suspend fun delete(id: Long): Boolean = suspendTransaction {
        ServiceDAO.findById(id)?.delete() != null
    }
}