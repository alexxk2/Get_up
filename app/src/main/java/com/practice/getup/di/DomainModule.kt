package com.practice.getup.di

import com.practice.getup.domain.storage.AddNewWorkoutUseCase
import com.practice.getup.domain.storage.DeleteAllWorkoutsUseCase
import com.practice.getup.domain.storage.DeleteWorkoutUseCase
import com.practice.getup.domain.storage.GetAllWorkoutsUseCase
import com.practice.getup.domain.storage.GetWorkoutUseCase
import com.practice.getup.domain.storage.UpdateWorkoutUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<AddNewWorkoutUseCase> { AddNewWorkoutUseCase(storageRepository = get()) }
    factory<DeleteAllWorkoutsUseCase> { DeleteAllWorkoutsUseCase(storageRepository = get()) }
    factory<DeleteWorkoutUseCase> { DeleteWorkoutUseCase(storageRepository = get()) }
    factory<GetAllWorkoutsUseCase> { GetAllWorkoutsUseCase(storageRepository = get()) }
    factory<GetWorkoutUseCase> { GetWorkoutUseCase(storageRepository = get()) }
    factory<UpdateWorkoutUseCase> { UpdateWorkoutUseCase(storageRepository = get()) }

}