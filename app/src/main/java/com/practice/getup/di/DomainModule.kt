package com.practice.getup.di

import com.practice.getup.domain.storage.AddNewWorkoutUseCase
import com.practice.getup.domain.storage.DeleteAllWorkoutsUseCase
import com.practice.getup.domain.storage.DeleteWorkoutUseCase
import com.practice.getup.domain.storage.GetAllWorkoutsUseCase
import com.practice.getup.domain.storage.GetWorkoutUseCase
import com.practice.getup.domain.storage.UpdateWorkoutUseCase
import com.practice.getup.domain.timer.GetGlobalTimeUseCase
import com.practice.getup.domain.timer.GetIndicatorProgressValueUseCase
import com.practice.getup.domain.timer.GetLocalTimeUseCase
import com.practice.getup.domain.timer.GetSoundStageUseCase
import com.practice.getup.domain.timer.GetStageListUseCase
import com.practice.getup.domain.timer.GetTimerStageUseCase
import com.practice.getup.domain.timer.GetWorkoutStagePositionUseCase
import com.practice.getup.domain.timer.PauseTimerUseCase
import com.practice.getup.domain.timer.PrepareTimerUseCase
import com.practice.getup.domain.timer.RestartTimerUseCase
import com.practice.getup.domain.timer.StartTimerUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<AddNewWorkoutUseCase> { AddNewWorkoutUseCase(storageRepository = get()) }
    factory<DeleteAllWorkoutsUseCase> { DeleteAllWorkoutsUseCase(storageRepository = get()) }
    factory<DeleteWorkoutUseCase> { DeleteWorkoutUseCase(storageRepository = get()) }
    factory<GetAllWorkoutsUseCase> { GetAllWorkoutsUseCase(storageRepository = get()) }
    factory<GetWorkoutUseCase> { GetWorkoutUseCase(storageRepository = get()) }
    factory<UpdateWorkoutUseCase> { UpdateWorkoutUseCase(storageRepository = get()) }

    factory<GetGlobalTimeUseCase> {GetGlobalTimeUseCase(timerRepository = get())  }
    factory<GetIndicatorProgressValueUseCase> {GetIndicatorProgressValueUseCase(timerRepository = get())  }
    factory<GetLocalTimeUseCase> {GetLocalTimeUseCase(timerRepository = get())  }
    factory<GetSoundStageUseCase> {GetSoundStageUseCase(timerRepository = get())  }
    factory<GetStageListUseCase> {GetStageListUseCase(timerRepository = get())  }
    factory<GetTimerStageUseCase> {GetTimerStageUseCase(timerRepository = get())  }
    factory<GetWorkoutStagePositionUseCase> {GetWorkoutStagePositionUseCase(timerRepository = get())  }
    factory<PauseTimerUseCase> {PauseTimerUseCase(timerRepository = get())  }
    factory<PrepareTimerUseCase> {PrepareTimerUseCase(timerRepository = get())  }
    factory<RestartTimerUseCase> {RestartTimerUseCase(timerRepository = get())  }
    factory<StartTimerUseCase> {StartTimerUseCase(timerRepository = get())  }
}