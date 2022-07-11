package com.lightfeather.wide_awakefinancials.ui.addtransaction

import androidx.lifecycle.ViewModel
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository

class AddTransactionViewModel(private val repository: ExpensesRepository) : ViewModel() {
    fun addTransaction(
        desc: String,
        categoryId: Int,
        amount: Double,
        transactionType: TransactionType
    ) {
        repository.addTransaction(desc, categoryId, amount, transactionType)
    }

    fun getTransactionCategories() = repository.getTransactionCategories()

}