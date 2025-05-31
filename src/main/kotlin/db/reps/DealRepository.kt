package db.reps

import db.Clients
import db.Deals
import db.dao.Deal
import db.dao.DealDAO
import db.dao.SimpleDeal
import db.suspendTransaction
import org.jetbrains.exposed.dao.id.EntityID

class DealRepository {

    suspend fun create(simpleDeal: SimpleDeal): SimpleDeal = suspendTransaction {
        DealDAO.new {
            date = simpleDeal.date
            commission = simpleDeal.commission
            description = simpleDeal.description
            clientId = EntityID(simpleDeal.clientId, Clients)
        }.toSimpleDeal()
    }

    suspend fun getById(id: Long): Deal? = suspendTransaction {
        DealDAO.findById(id)?.toDeal()
    }

    suspend fun getAll(): List<SimpleDeal> = suspendTransaction {
        DealDAO.all().map { it.toSimpleDeal() }
    }

    suspend fun update(id: Long, updated: SimpleDeal): Boolean = suspendTransaction {
        DealDAO.findByIdAndUpdate(id) { deal ->
            deal.date = updated.date
            deal.commission = updated.commission
            deal.description = updated.description
            deal.clientId = EntityID(updated.clientId, Clients)
        } != null
    }

    suspend fun delete(id: Long): Boolean = suspendTransaction {
        DealDAO.findById(id)?.delete() != null
    }

    suspend fun getDealsByClient(clientId: Long): List<SimpleDeal> = suspendTransaction {
        DealDAO.find { Deals.clientId eq clientId }
            .map { it.toSimpleDeal() }
    }
}