package db.reps

import db.dao.Discount
import db.dao.DiscountDAO
import db.suspendTransaction

class DiscountRepository {

    suspend fun create(discount: Discount): Discount = suspendTransaction {
        DiscountDAO.new {
            name = discount.name
            discountAmount = discount.discountAmount
            description = discount.description
        }.toDiscount()
    }

    suspend fun getById(id: Long): Discount? = suspendTransaction {
        DiscountDAO.findById(id)?.toDiscount()
    }

    suspend fun getAll(): List<Discount> = suspendTransaction {
        DiscountDAO.all().map { it.toDiscount() }
    }

    suspend fun update(id: Long, updated: Discount): Boolean = suspendTransaction {
        DiscountDAO.findByIdAndUpdate(id){ discount ->
            discount.apply {
                name = updated.name
                discountAmount = updated.discountAmount
                description = updated.description
            }
        } != null
    }

    suspend fun delete(id: Long): Boolean = suspendTransaction {
        DiscountDAO.findById(id)?.delete() != null
    }
}