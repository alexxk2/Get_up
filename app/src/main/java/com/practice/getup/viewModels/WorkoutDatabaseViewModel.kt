package com.practice.getup.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.practice.getup.database.Workout
import com.practice.getup.database.WorkoutDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class WorkoutDatabaseViewModel(private val workoutDao: WorkoutDao) : ViewModel() {

    val allWorkouts: LiveData<List<Workout>> = workoutDao.getAll().asLiveData()


    private fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutDao.insert(workout)
        }
    }

    private fun getNewItemInput(
        name: String,
        preparingTime: String,
        workTime: String,
        restTime: String,
        numberOfSets: String
    ) = Workout(
        name = name,
        preparingTime = preparingTime.toInt(),
        workTime = workTime.toInt(),
        restTime = restTime.toInt(),
        numberOfSets = numberOfSets.toInt()
    )

    fun addNewItem(
        name: String,
        preparingTime: String,
        workTime: String,
        restTime: String,
        numberOfSets: String
    ) {
        val newWorkout = getNewItemInput(
            name = name,
            preparingTime = preparingTime,
            workTime = workTime,
            restTime = restTime,
            numberOfSets = numberOfSets
        )
        insertWorkout(newWorkout)
    }

    fun isInputValid(
        name: String,
        preparingTime: String,
        workTime: String,
        restTime: String,
        numberOfSets: String
    ) =
        (name.isNotBlank() && preparingTime.isNotBlank() && workTime.isNotBlank() && restTime.isNotBlank() && numberOfSets.isNotBlank())


}

class WorkoutDatabaseViewModelFactory(private val workoutDao: WorkoutDao) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutDatabaseViewModel(workoutDao) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }
}