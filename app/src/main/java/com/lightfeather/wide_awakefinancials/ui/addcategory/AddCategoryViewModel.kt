package com.lightfeather.wide_awakefinancials.ui.addcategory

import androidx.lifecycle.ViewModel
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository

class AddCategoryViewModel(private val repository: ExpensesRepository) : ViewModel() {
    suspend fun addTransactionCategory(name: String, color: Int) =
        repository.addTransactionCategory(name, color)

}