package com.lightfeather.wide_awakefinancials.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = TransactionCategory::class,
        parentColumns = arrayOf("category_id"),
        childColumns = arrayOf("sourceId"),
        onDelete = ForeignKey.NO_ACTION,
        onUpdate = ForeignKey.SET_NULL
    )]
)
data class FinancialTransaction(
    val sourceId: Int,
    val description: String,
    val creationTime: Long,
    val amount: Double,
    val type: TransactionType,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

}
