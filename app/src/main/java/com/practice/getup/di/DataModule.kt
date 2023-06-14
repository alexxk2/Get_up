package com.practice.getup.di

import com.practice.getup.data.db.RoomStorage
import com.practice.getup.data.db.impl.RoomStorageImpl
import com.practice.getup.data.repositories.impl.StorageRepositoryImpl
import com.practice.getup.domain.repositories.StorageRepository
import org.koin.dsl.module

val dataModule = module {

    single<RoomStorage> {RoomStorageImpl(context = get())}

    single<StorageRepository>{StorageRepositoryImpl(roomStorage = get())}
}