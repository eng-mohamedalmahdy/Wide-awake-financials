package com.lightfeather.wide_awakefinancials.ui.addtransaction

import androidx.lifecycle.ViewModel
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository

class AddTransactionViewModel(private val repository: ExpensesRepository) : ViewModel() {
    fun addOrUpdateTransaction(
        isUpdating: Boolean,
        desc: String,
        categoryId: Int,
        creationTime: Long?,
        amount: Double,
        transactionType: TransactionType,
        id: Int?
    ) {
        if (isUpdating) repository.updateTransaction(
            id ?: return,
            desc,
            categoryId,
            creationTime ?: 0L,
            amount,
            transactionType
        )
        else repository.addTransaction(desc, categoryId, amount, transactionType)
    }

    fun getTransactionCategories() = repository.getTransactionCategories()

}