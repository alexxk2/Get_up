package com.practice.getup.di


import com.practice.getup.domain.models.Workout
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
import com.practice.getup.presentation.edit.view_model.OptionsViewModel
import com.practice.getup.presentation.main_menu.view_model.MainMenuViewModel
import com.practice.getup.presentation.timer.view_model.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<MainMenuViewModel> {
        MainMenuViewModel(
            deleteAllWorkoutsUseCase = get(),
            getAllWorkoutsUseCase = get()
        )
    }

    viewModel<OptionsViewModel> {
        OptionsViewModel(
            addNewWorkoutUseCase = get(),
            deleteWorkoutUseCase = get(),
            getWorkoutUseCase = get(),
            updateWorkoutUseCase = get()
        )
    }

    viewModel<TimerViewModel> { (workout: Workout) ->
        TimerViewModel(
            workout = workout,
            getGlobalTimeUseCase = get(),
            getIndicatorProgressValueUseCase = get(),
            getLocalTimeUseCase = get(),
            getSoundStageUseCase = get(),
            getStageListUseCase = get(),
            getTimerStageUseCase = get(),
            getWorkoutStagePositionUseCase = get(),
            pauseTimerUseCase = get(),
            prepareTimerUseCase = get(),
            restartTimerUseCase = get(),
            startTimerUseCase = get()
        )
    }
}