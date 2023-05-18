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

    private fun getNewWorkoutInput(
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

    private fun getUpdatedWorkoutInput(
        id: Int,
        name: String,
        preparingTime: String,
        workTime: String,
        restTime: String,
        numberOfSets: String
    ) = Workout(
        id = id,
        name = name,
        preparingTime = preparingTime.toInt(),
        workTime = workTime.toInt(),
        restTime = restTime.toInt(),
        numberOfSets = numberOfSets.toInt()
    )

    fun addNewWorkout(
        name: String,
        preparingTime: String,
        workTime: String,
        restTime: String,
        numberOfSets: String
    ) {
        val newWorkout = getNewWorkoutInput(
            name = name,
            preparingTime = preparingTime,
            workTime = workTime,
            restTime = restTime,
            numberOfSets = numberOfSets
        )
        insertWorkout(newWorkout)
    }

    fun isNumberInputValid(
        preparingTime: String,
        workTime: String,
        restTime: String,
        numberOfSets: String
    ) =
        (preparingTime.isNotBlank() && workTime.isNotBlank() && restTime.isNotBlank() && numberOfSets.isNotBlank()
                && preparingTime.toInt()>0 && workTime.toInt()>0 && restTime.toInt()>0 && numberOfSets.toInt()>0)

    fun isNameInputValid(
        name: String
    ) = (name.isNotBlank())

    fun retrieveWorkout(id: Int): LiveData<Workout>{
        return workoutDao.getWorkout(id).asLiveData()
    }

    fun deleteWorkout(workout: Workout){
        viewModelScope.launch {
            workoutDao.delete(workout)
        }
    }

    private fun updateWorkout(workout: Workout){
        viewModelScope.launch {
            workoutDao.update(workout)
        }
    }

    fun updateWorkout(
        id: Int,
        name: String,
        preparingTime: String,
        workTime: String,
        restTime: String,
        numberOfSets: String

    ) {
        val updatedWorkout = getUpdatedWorkoutInput(
            id = id,
            name = name,
            preparingTime = preparingTime,
            workTime = workTime,
            restTime = restTime,
            numberOfSets = numberOfSets
        )
        updateWorkout(updatedWorkout)
    }

    fun deleteAll(){
        viewModelScope.launch {
            workoutDao.deleteAll()
        }
    }

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