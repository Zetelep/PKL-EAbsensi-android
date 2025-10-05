package com.zulfa.eabsensi

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.zulfa.eabsensi.core.di.dataStoreModule
import com.zulfa.eabsensi.core.di.networkModule
import com.zulfa.eabsensi.core.di.repositoryModule
import com.zulfa.eabsensi.presentation.di.useCaseModule
import com.zulfa.eabsensi.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    dataStoreModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}