package com.lightfeather.wide_awakefinancials.domain.core

import android.app.Application
import com.lightfeather.wide_awakefinancials.BuildConfig
import com.lightfeather.wide_awakefinancials.domain.di.dataModule
import com.lightfeather.wide_awakefinancials.domain.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WideAwakeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WideAwakeApplication)
            modules(dataModule, databaseModule)
        }
    }
}