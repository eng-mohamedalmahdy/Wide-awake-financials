package com.lightfeather.wide_awakefinancials.domain.repositories

import android.util.Log
import com.lightfeather.wide_awakefinancials.domain.model.*
import com.lightfeather.wide_awakefinancials.domain.persistence.TransactionsDAO
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random


private const val TAG = "ExpensesRepository"

class ExpensesRepository(private val transactionsDAO: TransactionsDAO) {
    private val tipsList = listOf(
        "Get Paid What You're Worth and Spend Less Than You Earn",
        "Stick to a Budget",
        "Pay Off Credit Card Debt",
        "Contribute to a Retirement Plan",
        "Have a Savings Plan",
        "Maximize Your Employment Benefits",
        "Keep Good Records"
    )

    fun getExpensesData() = transactionsDAO.getExpensesSummeryWithColors()

    fun getIncomeData() = transactionsDAO.getIncomeSummeryWithColors()

    fun getAllTransactions() = transactionsDAO.getAllTransactions()
    fun getAllTransactionsWithColors() = transactionsDAO.getAllTransactionsWithColors()

    fun addTransactionCategory(name: String, color: Int) {
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
    fun getInsights(): String {
        return tipsList.random()
    }

    suspend fun deleteTransaction(transaction: ColoredFinancialTransaction) =
        transactionsDAO.deleteTransaction(transaction.toFinancialTransaction())

    fun updateTransaction(
        id: Int,
        desc: String,
        categoryId: Int,
        creationTime: Long,
        amount: Double,
        transactionType: TransactionType
    ) {
        val transaction = FinancialTransaction(
            categoryId,
            desc,
            creationTime,
            amount,
            transactionType
        )
        transaction.id = id
        transactionsDAO.updateTransaction(transaction)
    }

    fun getAverageSpending(): Flow<Double> = transactionsDAO.getAverageSpending()
    fun getHighestTransaction(): Flow<Double> = transactionsDAO.getHighestTransaction()

    fun getLowestTransaction(): Flow<Double> = transactionsDAO.getLowestTransaction()

    fun getHighestCategory(): Flow<SelectedCategory> = transactionsDAO.getHighestCategory()

    fun getLowestCategory(): Flow<SelectedCategory> = transactionsDAO.getLowestCategory()
    fun getCategoriesName(): Flow<List<String>> = transactionsDAO.getCategoriesName()
    fun getExpensesByCategoryName(name: String) = transactionsDAO.getExpensesByCategoryName(name)
    fun getIncomeByCategoryName(name: String) = transactionsDAO.getIncomeByCategoryName(name)
}