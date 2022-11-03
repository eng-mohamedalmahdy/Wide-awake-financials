package com.lightfeather.wide_awakefinancials.ui.insights

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.*
import com.lightfeather.wide_awakefinancials.domain.model.ColoredFinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "InsightsViewModel"
class InsightsViewModel(private val repo: ExpensesRepository) : ViewModel() {

    fun getInsights(): String = repo.getInsights()
    fun getIncomeData() = repo.getIncomeData().map { transactions ->
        val colors = transactions.map { Color.parseColor(it.categoryColor) }
        val entries =
            transactions.groupBy { SimpleDateFormat("dd/MMM/YY").format(Date(it.creationTime)) }
        Pair(colors, entries)
    }


    fun getExpensesData() =
        repo.getExpensesData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries = transactions.groupBy {
                SimpleDateFormat("dd/MMM/YY").format(Date(it.creationTime))
            }
            Pair(colors, entries)
        }

    fun getTotalExpenses(): Flow<Double> {
        return repo.getExpensesData().map { it.sumOf { it.amount } }
    }

    fun getTotalIncome(): Flow<Double> {
        return repo.getIncomeData().map { it.sumOf { it.amount } }
    }

    fun getTotalExpensesAndIncome(): Flow<Double> {
        return repo.getIncomeData().map { it.sumOf { it.amount } }
            .combine(repo.getExpensesData().map { it.sumOf { it.amount } }) { a, b -> a - b }
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
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }
                    .distinctBy { it.label }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }



    fun getExpensesDataColored(): Flow<PieData> {
        return repo.getExpensesData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }

    fun getIncomeDataColored(): Flow<PieData> {

        return repo.getIncomeData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }

}