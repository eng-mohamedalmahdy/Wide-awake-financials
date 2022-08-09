package com.lightfeather.wide_awakefinancials.domain.model

import java.io.Serializable

data class ColoredFinancialTransaction(
    val id: Int,
    val categoryId: Int,
    val categoryColor: String,
    val description: String,
    val categoryName: String,
    val creationTime: Long,
    val amount: Double,
    val type: TransactionType,
) : TransactionsListModel(), Serializable {
    fun toFinancialTransaction(): FinancialTransaction = FinancialTransaction(
        sourceId = categoryId,
        description = description,
        creationTime = creationTime,
        amount, type
    ).also {
        it.id = id
    }
}

data class DateListItem(val date: String) : TransactionsListModel()
