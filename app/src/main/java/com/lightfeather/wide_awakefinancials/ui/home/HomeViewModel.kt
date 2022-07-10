package com.lightfeather.wide_awakefinancials.ui.home

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository

class HomeViewModel(private val repo: ExpensesRepository) : ViewModel() {
    fun getExpensesData(): PieData {
        val pieDataSet = PieDataSet(
            repo.getExpensesData().map {
                PieEntry(it.amount.toFloat())
            },
            "Expenses"
        )
        val data = PieData()
        data.addDataSet(pieDataSet)
        return data
    }

    fun getIncomeData(): PieData {
        return getExpensesData()
    }

    fun getAllTransactions(): List<FinancialTransaction> {
        return listOf()
    }
}