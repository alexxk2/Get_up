package com.practice.getup.viewModels

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.getup.model.Options

class OptionsViewModel : ViewModel() {

    private val _options = MutableLiveData(Options.DEFAULT)
    val options: LiveData<Options> = _options

    private val _totalTime = MutableLiveData<String>()
    val totalTime: LiveData<String> = _totalTime

    private var preparationTimeInput =
        _options.value?.preparingTime ?: Options.DEFAULT.preparingTime
    private var workTimeInput = _options.value?.preparingTime ?: Options.DEFAULT.workTime
    private var restTimeInput = _options.value?.restTime ?: Options.DEFAULT.restTime
    private var numberOfSetsInput = _options.value?.numberOfSets ?: Options.DEFAULT.numberOfSets

    init {
        calculateTotalTime()
    }

    fun getPreparationTimeInput(input: Editable?) {
        preparationTimeInput = input.toString().toIntOrNull() ?: Options.DEFAULT.preparingTime
    }

    fun getWorkTimeInput(input: Editable?) {
        workTimeInput = input.toString().toIntOrNull() ?: Options.DEFAULT.workTime
    }

    fun getRestTimeInput(input: Editable?) {
        restTimeInput = input.toString().toIntOrNull() ?: Options.DEFAULT.restTime
    }

    fun getSetsNumberInput(input: Editable?) {
        numberOfSetsInput = input.toString().toIntOrNull() ?: Options.DEFAULT.numberOfSets
    }


    fun calculateTotalTime() {
        val totalSeconds = (workTimeInput + restTimeInput) * numberOfSetsInput + preparationTimeInput
        val secondsToShow = totalSeconds % 60
        val minutesToShow = (totalSeconds / 60) % 60
        val hoursToShow = totalSeconds / 3600

        _totalTime.value = "$hoursToShow : $minutesToShow : $secondsToShow"
    }

    fun updateOptions(){
        _options.value = Options(preparationTimeInput,workTimeInput,restTimeInput,numberOfSetsInput)
    }

}