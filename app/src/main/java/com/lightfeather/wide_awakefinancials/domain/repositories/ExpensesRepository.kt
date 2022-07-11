package com.lightfeather.wide_awakefinancials.domain.repositories

import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.TransactionCategory
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import com.lightfeather.wide_awakefinancials.domain.persistence.TransactionsDAO

class ExpensesRepository(private val transactionsDAO: TransactionsDAO) {
    fun getExpensesData(): List<FinancialTransaction> =
        listOf(
            FinancialTransaction(1, "A", 111, 20.0, TransactionType.EXPENSE),
            FinancialTransaction(1, "B", 111, 20.0, TransactionType.EXPENSE)
        )

    fun getIncomeData(): List<FinancialTransaction> =
        listOf(FinancialTransaction(1, "B", 111, 20.0, TransactionType.INCOME))

    suspend fun addTransactionCategory(name: String, color: Int) {
        val hexColor = java.lang.String.format("#%06X", 0xFFFFFF and color)
        val category = TransactionCategory(name = name, color = hexColor)
        transactionsDAO.insertTransactionCategory(category)
    }

    fun addTransaction(
        desc: String,
        categoryId: Int,
        amount: Double,
        transactionType: TransactionType
    ) {
        val transaction = FinancialTransaction(
            categoryId,
            desc,
            System.currentTimeMillis(),
            amount,
            transactionType
        )
        transactionsDAO.insertTransaction(transaction)
    }

    fun getTransactionCategories() = transactionsDAO.getAllTransactionCategories()

}