package com.lightfeather.wide_awakefinancials.domain.repositories

import android.util.Log
import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.TransactionCategory
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import com.lightfeather.wide_awakefinancials.domain.persistence.TransactionsDAO
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
        Log.d(TAG, "addTransaction: $categoryId")
        transactionsDAO.insertTransaction(transaction)
    }

    fun getTransactionCategories() = transactionsDAO.getAllTransactionCategories()
    fun getInsights(): String {
        return tipsList.random()
    }

}