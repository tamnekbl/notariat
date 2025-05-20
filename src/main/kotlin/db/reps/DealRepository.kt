package db.reps

import db.dao.ClientDAO
import db.dao.Deal
import db.dao.DealDAO
import db.suspendTransaction

class DealRepository {

    suspend fun create(deal: Deal): Deal = suspendTransaction {
        DealDAO.new {
            date = deal.date
            commission = deal.commission
            description = deal.description
            client = deal.clientId?.let { ClientDAO.findById(it) }
        }.toDeal()
    }

    suspend fun getById(id: Int): Deal? = suspendTransaction {
        DealDAO.findById(id)?.toDeal()
    }

    suspend fun getAll(): List<Deal> = suspendTransaction {
        DealDAO.all().map { it.toDeal() }
    }

    suspend fun update(id: Int, updated: Deal): Boolean = suspendTransaction {
        DealDAO.findByIdAndUpdate(id) { deal ->
            deal.date = updated.date
            deal.commission = updated.commission
            deal.description = updated.description
            deal.client = updated.clientId?.let { ClientDAO.findById(it) }
        } != null
    }

    suspend fun delete(id: Int): Boolean = suspendTransaction {
        DealDAO.findById(id)?.delete() != null
    }
}