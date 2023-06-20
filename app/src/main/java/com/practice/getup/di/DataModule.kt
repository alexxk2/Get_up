package com.practice.getup.di

import com.practice.getup.data.db.RoomStorage
import com.practice.getup.data.db.impl.RoomStorageImpl
import com.practice.getup.data.repositories.impl.SettingsRepositoryImpl
import com.practice.getup.data.repositories.impl.StorageRepositoryImpl
import com.practice.getup.data.repositories.impl.TimerRepositoryImpl
import com.practice.getup.data.settings.LocalSettings
import com.practice.getup.data.settings.impl.LocalSettingsImpl
import com.practice.getup.data.timer.Timer
import com.practice.getup.data.timer.impl.TimerImpl
import com.practice.getup.domain.repositories.SettingsRepository
import com.practice.getup.domain.repositories.StorageRepository
import com.practice.getup.domain.repositories.TimerRepository
import org.koin.dsl.module

val dataModule = module {

    single<RoomStorage> { RoomStorageImpl(context = get()) }

    single<StorageRepository> { StorageRepositoryImpl(roomStorage = get()) }


    single <Timer> { TimerImpl(context = get()) }

    single <TimerRepository> { TimerRepositoryImpl(timer = get()) }


    single<LocalSettings> {LocalSettingsImpl(context = get())  }

    single<SettingsRepository> {SettingsRepositoryImpl(localSettings = get())  }
}