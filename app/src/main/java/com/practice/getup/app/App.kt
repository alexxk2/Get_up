package com.practice.getup.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.practice.getup.data.db.WorkoutDatabase
import com.practice.getup.di.dataModule
import com.practice.getup.di.domainModule
import com.practice.getup.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.*



class App: Application() {

    val workoutDatabase: WorkoutDatabase by lazy { WorkoutDatabase.getDataBase(this) }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(
                dataModule,
                domainModule,
                presentationModule
            ))
        }
    }
}