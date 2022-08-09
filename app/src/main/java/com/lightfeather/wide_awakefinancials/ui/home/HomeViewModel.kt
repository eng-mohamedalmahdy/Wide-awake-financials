package com.lightfeather.wide_awakefinancials.ui.home

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.lightfeather.wide_awakefinancials.domain.model.ColoredFinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

private const val TAG = "HomeViewModel"

class HomeViewModel(private val repo: ExpensesRepository) : ViewModel() {
    fun getExpensesData(): Flow<PieData> {
        return repo.getExpensesData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }

    fun getIncomeData(): Flow<PieData> {
        return repo.getIncomeData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }

    fun getTotalExpenses(): Flow<Double> {
        return repo.getExpensesData().map { it.sumOf { it.amount } }
    }

    fun getTotalIncome(): Flow<Double> {
        Log.d(TAG, "getTotalIncome:")
        return repo.getIncomeData().map { it.sumOf { it.amount } }
    }

    fun getAllTransactionsWithColors() = repo.getAllTransactionsWithColors()
    suspend fun deleteItem(coloredFinancialTransaction: ColoredFinancialTransaction) =
        repo.deleteTransaction(coloredFinancialTransaction)

    fun insertTransaction(item: ColoredFinancialTransaction) =
        repo.addTransaction(item.description, item.categoryId, item.amount, item.type)

    fun getAllData(): Flow<PieData> {
        return repo.getAllTransactionsWithColors().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }.distinct()
            val entries =
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }.distinctBy { it.label }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }

    fun getTotalExpensesAndIncome(): Flow<Double> {
        return repo.getIncomeData().map { it.sumOf { it.amount } }
            .combine(repo.getExpensesData().map { it.sumOf { it.amount } }) { a, b -> b - a }
    }


}