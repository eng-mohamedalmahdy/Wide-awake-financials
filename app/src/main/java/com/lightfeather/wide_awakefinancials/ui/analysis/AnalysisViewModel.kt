package com.lightfeather.wide_awakefinancials.ui.analysis

import android.graphics.Color
import android.icu.text.MessageFormat.format
import android.text.format.DateFormat
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lightfeather.wide_awakefinancials.domain.model.SelectedCategory
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "AnalysisViewModel"

class AnalysisViewModel(private val repo: ExpensesRepository) : ViewModel() {
    fun getAverageSpending(): Flow<Double> = repo.getAverageSpending()

    fun getHighestTransaction(): Flow<Double> = repo.getHighestTransaction()

    fun getLowestTransaction(): Flow<Double> = repo.getLowestTransaction()

    fun getHighestCategory(): Flow<SelectedCategory> = repo.getHighestCategory()

    fun getLowestCategory(): Flow<SelectedCategory> = repo.getLowestCategory()

    fun getCategoriesName(): Flow<List<String>> = repo.getCategoriesName()


    fun getLiabilitiesData(): Flow<List<PieData>> {
        val f1 = repo.getIncomeData().map { transactions ->
            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions.map { item ->
                    PieEntry(
                        item.amount.toFloat(),
                        item.categoryName
                    )
                }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }


        val f2 = repo.getExpensesData().map { transactions ->
            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })
        }
        val f3 = repo.getAllTransactionsWithColors().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }.distinct()
            val entries =
                transactions.map { item -> PieEntry(item.amount.toFloat(), item.categoryName) }
                    .distinctBy { it.label }
            PieData(PieDataSet(entries, "").apply {
                this.colors = colors
            })

        }
        return combine(f1, f2, f3) { arrayOfPieData -> arrayOfPieData.toList() }
    }

    fun getLiabilitiesNumbers() =
        combine(
            getTotalExpenses(),
            getTotalIncome(),
            getTotalExpensesAndIncome()
        ) { doubles -> doubles.toList() }

    private fun getTotalExpenses(): Flow<Double> {
        return repo.getExpensesData().map { it.sumOf { it.amount } }
    }

    private fun getTotalIncome(): Flow<Double> {
        return repo.getIncomeData().map { it.sumOf { it.amount } }
    }

    private fun getTotalExpensesAndIncome(): Flow<Double> {
        return repo.getIncomeData().map { it.sumOf { it.amount } }
            .combine(repo.getExpensesData().map { it.sumOf { it.amount } }) { a, b -> a - b }
    }

    fun getIncomeDataGroupedByDateFilteredByCategories(filterWord: String = "") =
        repo.getIncomeData().map { transactions ->
            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions.filter { it.categoryName.contains(filterWord) }
                    .groupBy { SimpleDateFormat("dd/MMM/YY").format(Date(it.creationTime)) }
            Pair(colors, entries)
        }


    fun getExpensesDataGroupedByDateFilteredByCategories(filterWord: String = "") =
        repo.getExpensesData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries =
                transactions
                    .groupBy {
                        DateFormat.format("dd/MM/yyyy", it.creationTime).toString()
                    }
            Pair(colors, entries)
        }

    fun getDailyAnalysisData() =
        combine(
            getExpensesDataGroupedByDateFilteredByCategories(),
            getIncomeDataGroupedByDateFilteredByCategories()
        ) { data -> data.toList() }
}

