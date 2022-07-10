package com.lightfeather.wide_awakefinancials.domain.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.TransactionCategory

@Database(
    entities = [FinancialTransaction::class,
        TransactionCategory::class], version = 1
)
abstract class FinancialTransactionsDatabase : RoomDatabase() {
    abstract fun transactionsDAO(): TransactionsDAO
}

