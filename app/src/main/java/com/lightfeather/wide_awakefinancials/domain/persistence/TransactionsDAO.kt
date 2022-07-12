package com.lightfeather.wide_awakefinancials.domain.persistence

import androidx.room.*
import com.lightfeather.wide_awakefinancials.domain.model.ColoredFinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDAO {
    @Insert
    fun insertTransactionCategory(category: TransactionCategory)

    @Query("SELECT * FROM TransactionCategory")
    fun getAllTransactionCategories(): Flow<List<TransactionCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: FinancialTransaction)

    @Delete
    fun deleteTransaction(transaction: FinancialTransaction)

    @Delete
    fun deleteCategory(transactionCategory: TransactionCategory)

    @Query("SELECT * FROM FinancialTransaction")
    fun getAllTransactions(): Flow<List<FinancialTransaction>>

    @Query(
        "SELECT DISTINCT" +
                " color as categoryColor," +
                " description as description," +
                " TransactionCategory.name as categoryName,"+
                " creationTime as creationTime," +
                " amount as amount," +
                " type as type" +
                " FROM FinancialTransaction,TransactionCategory" +
                " WHERE TransactionCategory.category_id = FinancialTransaction.sourceId " +
                " ORDER BY creationTime"
    )
    fun getAllTransactionsWithColors(): Flow<List<ColoredFinancialTransaction>>

    @Query(
        "SELECT DISTINCT" +
                " color as categoryColor," +
                " description as description," +
                " TransactionCategory.name as categoryName, "+
                " creationTime as creationTime," +
                " SUM(amount) as amount," +
                " type as type" +
                " FROM FinancialTransaction,TransactionCategory" +
                " WHERE TransactionCategory.category_id = FinancialTransaction.sourceId " +
                " GROUP BY TransactionCategory.category_id"
    )
    fun getAllTransactionsSummeryWithColors(): Flow<List<ColoredFinancialTransaction>>

    @Query(
        "SELECT DISTINCT" +
                " color as categoryColor," +
                " description as description," +
                " creationTime as creationTime," +
                " SUM(amount) as amount," +
                " type as type," +
                " name as categoryName"+
                " FROM FinancialTransaction,TransactionCategory" +
                " WHERE TransactionCategory.category_id = FinancialTransaction.sourceId AND type = 'EXPENSE'" +
                " GROUP BY TransactionCategory.category_id"
    )
    fun getExpensesSummeryWithColors(): Flow<List<ColoredFinancialTransaction>>

    @Query(
        "SELECT DISTINCT" +
                " color as categoryColor," +
                " description as description," +
                " creationTime as creationTime," +
                " SUM(amount) as amount," +
                " type as type," +
                " name as categoryName"+
                " FROM FinancialTransaction,TransactionCategory" +
                " WHERE TransactionCategory.category_id = FinancialTransaction.sourceId AND type = 'INCOME'" +
                " GROUP BY TransactionCategory.category_id"
    )
    fun getIncomeSummeryWithColors(): Flow<List<ColoredFinancialTransaction>>

}