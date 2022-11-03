package com.lightfeather.wide_awakefinancials.domain.persistence

import androidx.room.*
import com.lightfeather.wide_awakefinancials.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Dao
interface TransactionsDAO {
    @Insert
    fun insertTransactionCategory(category: TransactionCategory)

    @Query("SELECT * FROM TransactionCategory")
    fun getAllTransactionCategories(): Flow<List<TransactionCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: FinancialTransaction)

    @Delete
    suspend fun deleteTransaction(transaction: FinancialTransaction)

    @Delete
    fun deleteCategory(transactionCategory: TransactionCategory)

    @Query("SELECT * FROM FinancialTransaction")
    fun getAllTransactions(): Flow<List<FinancialTransaction>>

    @Query(
        "SELECT DISTINCT" +
                " id, " +
                "category_id as categoryId, " +
                " color as categoryColor," +
                " description as description," +
                " TransactionCategory.name as categoryName," +
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
                " id, " +
                "category_id as categoryId," +
                " color as categoryColor," +
                " description as description," +
                " TransactionCategory.name as categoryName, " +
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
                " id," +
                "category_id as categoryId, " +
                " color as categoryColor," +
                " description as description," +
                " creationTime as creationTime," +
                " SUM(amount) as amount," +
                " type as type," +
                " name as categoryName" +
                " FROM FinancialTransaction,TransactionCategory" +
                " WHERE TransactionCategory.category_id = FinancialTransaction.sourceId AND type = 'EXPENSE'" +
                " GROUP BY TransactionCategory.category_id"
    )
    fun getExpensesSummeryWithColors(): Flow<List<ColoredFinancialTransaction>>

    @Query(
        "SELECT DISTINCT" +
                " id ," +
                "category_id as categoryId, " +
                " color as categoryColor," +
                " description as description," +
                " creationTime as creationTime," +
                " SUM(amount) as amount," +
                " type as type," +
                " name as categoryName" +
                " FROM FinancialTransaction,TransactionCategory" +
                " WHERE TransactionCategory.category_id = FinancialTransaction.sourceId AND type = 'INCOME'" +
                " GROUP BY TransactionCategory.category_id"
    )
    fun getIncomeSummeryWithColors(): Flow<List<ColoredFinancialTransaction>>

    @Update
    fun updateTransaction(transaction: FinancialTransaction)

    @Query("SELECT AVG(amount) FROM FinancialTransaction")
    fun getAverageSpending(): Flow<Double>

    @Query("SELECT MAX(amount) FROM FinancialTransaction")
    fun getHighestTransaction(): Flow<Double>

    @Query("SELECT MIN(amount) FROM FinancialTransaction")
    fun getLowestTransaction(): Flow<Double>


    @Query(
        "SELECT ctgs.name as name , MAX(ctgs.summed)  as amount \n" +
                "FROM\n" +
                "(SELECT DISTINCT  TransactionCategory.name , SUM(amount) as summed\n" +
                "FROM FinancialTransaction, TransactionCategory\n" +
                "WHERE TransactionCategory.category_id = sourceId  AND FinancialTransaction.type = \"EXPENSE\"\n" +
                "GROUP BY  sourceId) as ctgs"
    )
    fun getHighestCategory(): Flow<SelectedCategory>


    @Query(
        "SELECT ctgs.name as name , MIN(ctgs.summed)  as amount \n" +
                "FROM\n" +
                "(SELECT DISTINCT  TransactionCategory.name , SUM(amount) as summed\n" +
                "FROM FinancialTransaction, TransactionCategory\n" +
                "WHERE TransactionCategory.category_id = sourceId  AND FinancialTransaction.type = \"EXPENSE\"\n" +
                "GROUP BY  sourceId) as ctgs"
    )
    fun getLowestCategory(): Flow<SelectedCategory>

    @Query("SELECT name FROM TransactionCategory")
    fun getCategoriesName(): Flow<List<String>>

    @Query(
        "SELECT sourceId, description , creationTime, amount ,type FROM FinancialTransaction  ft\n" +
                "INNER JOIN TransactionCategory tc ON ft.sourceId = tc.category_id\n" +
                "WHERE tc.name=:name AND ft.type = \"EXPENSE\""
    )
    fun getExpensesByCategoryName(name: String): Flow<List<FinancialTransaction>>


    @Query(
        "SELECT sourceId, description , creationTime, amount ,type FROM FinancialTransaction  ft\n" +
                "INNER JOIN TransactionCategory tc ON ft.sourceId = tc.category_id\n" +
                "WHERE tc.name=:name AND ft.type = \"INCOME\""
    )
    fun getIncomeByCategoryName(name: String): Flow<List<FinancialTransaction>>


}