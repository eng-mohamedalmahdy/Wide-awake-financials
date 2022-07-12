package com.lightfeather.wide_awakefinancials.domain.model

data class ColoredFinancialTransaction(
    val categoryColor: String,
    val description: String,
    val categoryName: String,
    val creationTime: Long,
    val amount: Double,
    val type: TransactionType,
)
