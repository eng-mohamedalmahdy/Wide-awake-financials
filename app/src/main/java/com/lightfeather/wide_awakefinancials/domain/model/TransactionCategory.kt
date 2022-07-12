package com.lightfeather.wide_awakefinancials.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionCategory(
    val name: String,
    val color: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    var id: Int? = null

}
