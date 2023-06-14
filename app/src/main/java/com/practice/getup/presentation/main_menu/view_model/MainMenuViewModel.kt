package com.practice.getup.presentation.main_menu.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practice.getup.database.Workout
import com.practice.getup.domain.storage.AddNewWorkoutUseCase
import com.practice.getup.domain.storage.DeleteAllWorkoutsUseCase
import com.practice.getup.domain.storage.DeleteWorkoutUseCase
import com.practice.getup.domain.storage.GetAllWorkoutsUseCase
import com.practice.getup.domain.storage.GetWorkoutUseCase
import com.practice.getup.domain.storage.UpdateWorkoutUseCase
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val deleteAllWorkoutsUseCase: DeleteAllWorkoutsUseCase,
    getAllWorkoutsUseCase: GetAllWorkoutsUseCase,


): ViewModel() {

    val allWorkouts: LiveData<List<Workout>> = getAllWorkoutsUseCase.execute().asLiveData()


    fun deleteAll(){
        viewModelScope.launch {
            deleteAllWorkoutsUseCase.execute()
        }
    }

}