package com.practice.getup.presentation.main_menu.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.settings.SetLanguageUseCase
import com.practice.getup.domain.settings.SetNightModeUseCase
import com.practice.getup.domain.storage.DeleteAllWorkoutsUseCase
import com.practice.getup.domain.storage.GetAllWorkoutsUseCase
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val deleteAllWorkoutsUseCase: DeleteAllWorkoutsUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setNightModeUseCase: SetNightModeUseCase,
    getAllWorkoutsUseCase: GetAllWorkoutsUseCase

): ViewModel() {

    val allWorkouts: LiveData<List<Workout>> = getAllWorkoutsUseCase.execute().asLiveData()


    fun deleteAll(){
        viewModelScope.launch {
            deleteAllWorkoutsUseCase.execute()
        }
    }

    fun setAppLanguage(){
        setLanguageUseCase.execute()
    }

    fun setAppDayOrNightMode(){
        setNightModeUseCase.execute()
    }

}