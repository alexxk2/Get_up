package com.practice.getup.presentation.edit.view_model

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.storage.AddNewWorkoutUseCase
import com.practice.getup.domain.storage.DeleteWorkoutUseCase
import com.practice.getup.domain.storage.GetWorkoutUseCase
import com.practice.getup.domain.storage.UpdateWorkoutUseCase
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val addNewWorkoutUseCase: AddNewWorkoutUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    private val getWorkoutUseCase: GetWorkoutUseCase,
    private val updateWorkoutUseCase: UpdateWorkoutUseCase
) : ViewModel() {


    private val _totalTime = MutableLiveData<String>()
    val totalTime: LiveData<String> = _totalTime

    private val _workout = MutableLiveData<Workout>()
    val workout: LiveData<Workout> = _workout

    private var preparationTimeInput =
        0
    private var workTimeInput = 0
    private var restTimeInput = 0
    private var numberOfSetsInput = 0

    init {
        calculateTotalTime()
    }

    fun getPreparationTimeInput(input: Editable?) {
        preparationTimeInput = input.toString().toIntOrNull() ?: 0
    }

    fun getWorkTimeInput(input: Editable?) {
        workTimeInput = input.toString().toIntOrNull() ?: 0
    }

    fun getRestTimeInput(input: Editable?) {
        restTimeInput = input.toString().toIntOrNull() ?: 0
    }

    fun getSetsNumberInput(input: Editable?) {
        numberOfSetsInput = input.toString().toIntOrNull() ?: 0
    }


    fun calculateTotalTime() {
        val totalSeconds =
            (workTimeInput + restTimeInput) * numberOfSetsInput + preparationTimeInput
        val secondsToShow = (totalSeconds % 60).toString().padStart(2, '0')
        val minutesToShow = ((totalSeconds / 60) % 60).toString().padStart(2, '0')
        val hoursToShow = (totalSeconds / 3600).toString().padStart(2, '0')

        _totalTime.value = when {
            (totalSeconds / 3600) > 0 -> "$hoursToShow : $minutesToShow : $secondsToShow"
            else -> "$minutesToShow : $secondsToShow"
        }
    }

    private fun insertWorkout(workout: Workout) {
        viewModelScope.launch {
            addNewWorkoutUseCase.execute(workout)
        }
    }

    fun retrieveWorkout(id: Int): LiveData<Workout> {
        viewModelScope.launch {
            val result = getWorkoutUseCase.execute(id)
            _workout.value = result
        }
        return workout
    }

    fun deleteWorkout(workout: Workout){
        viewModelScope.launch {
            deleteWorkoutUseCase.execute(workout)
        }
    }

    private fun updateWorkout(workout: Workout){
        viewModelScope.launch {
            updateWorkoutUseCase.execute(workout)
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

}