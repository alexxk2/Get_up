package com.practice.getup.di


import com.practice.getup.presentation.edit.view_model.OptionsViewModel
import com.practice.getup.presentation.main_menu.view_model.MainMenuViewModel
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
}