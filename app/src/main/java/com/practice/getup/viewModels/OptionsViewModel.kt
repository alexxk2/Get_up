package com.practice.getup.viewModels

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.practice.getup.model.Options

class OptionsViewModel : ViewModel() {


    private val _totalTime = MutableLiveData<String>()
    val totalTime: LiveData<String> = _totalTime

    private var preparationTimeInput =
        0
    private var workTimeInput = 0
    private var restTimeInput = 0
    private var numberOfSetsInput = 0

    init {
        calculateTotalTime()
    }

    fun getPreparationTimeInput(input: Editable?) {
        preparationTimeInput = input.toString().toInt()
    }

    fun getWorkTimeInput(input: Editable?) {
        workTimeInput = input.toString().toInt()
    }

    fun getRestTimeInput(input: Editable?) {
        restTimeInput = input.toString().toInt()
    }

    fun getSetsNumberInput(input: Editable?) {
        numberOfSetsInput = input.toString().toInt()
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
}