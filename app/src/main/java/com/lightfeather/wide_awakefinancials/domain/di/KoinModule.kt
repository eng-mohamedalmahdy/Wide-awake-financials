package com.lightfeather.wide_awakefinancials.domain.di

import android.app.Application
import androidx.room.Room
import com.lightfeather.wide_awakefinancials.domain.persistence.FinancialTransactionsDatabase
import com.lightfeather.wide_awakefinancials.domain.persistence.TransactionsDAO
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository
import com.lightfeather.wide_awakefinancials.ui.addcategory.AddCategoryViewModel
import com.lightfeather.wide_awakefinancials.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { ExpensesRepository(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AddCategoryViewModel(get()) }
}
val databaseModule = module {
    fun provideDataBase(application: Application): FinancialTransactionsDatabase {
        return Room.databaseBuilder(
            application,
            FinancialTransactionsDatabase::class.java,
            "transactions.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(dataBase: FinancialTransactionsDatabase): TransactionsDAO {
        return dataBase.transactionsDAO()
    }
    single { provideDataBase(androidApplication()) }
    single { provideDao(get()) }

}