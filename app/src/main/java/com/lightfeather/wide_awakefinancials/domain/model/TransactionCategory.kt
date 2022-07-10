package com.lightfeather.wide_awakefinancials.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TransactionCategory(
    val name: String,
    val color: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
