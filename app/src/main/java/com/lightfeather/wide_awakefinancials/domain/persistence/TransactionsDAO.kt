package com.lightfeather.wide_awakefinancials.domain.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lightfeather.wide_awakefinancials.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDAO {
    @Insert
    fun insertTransactionCategory(category: TransactionCategory)

    @Query("SELECT * FROM TransactionCategory")
    fun getAllTransactionCategories(): Flow<List<TransactionCategory>>
}