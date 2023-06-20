package com.practice.getup.di


import com.practice.getup.domain.models.Workout
import com.practice.getup.presentation.edit.view_model.OptionsViewModel
import com.practice.getup.presentation.main_menu.view_model.MainMenuViewModel
import com.practice.getup.presentation.settings.view_model.SettingsViewModel
import com.practice.getup.presentation.timer.view_model.TimerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<MainMenuViewModel> {
        MainMenuViewModel(
            deleteAllWorkoutsUseCase = get(),
            getAllWorkoutsUseCase = get(),
            setLanguageUseCase = get(),
            setNightModeUseCase = get()
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

    viewModel<SettingsViewModel> {
        SettingsViewModel(
        getCurrentLanguageUseCase = get(),
        getCurrentThemeUseCase = get(),
        switchLanguageUseCase = get(),
        switchThemeUseCase = get()
        )
    }
}