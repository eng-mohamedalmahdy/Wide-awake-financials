package com.lightfeather.wide_awakefinancials.domain.core

import android.content.Context
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.lightfeather.wide_awakefinancials.domain.di.dataModule
import com.lightfeather.wide_awakefinancials.domain.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

class WideAwakeApplication : LocalizationApplication() {
    override fun getDefaultLanguage(context: Context): Locale {
        return Locale.getDefault()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WideAwakeApplication)
            modules(dataModule, databaseModule)
        }
    }
}