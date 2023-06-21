package com.practice.getup.presentation.timer.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.timer.GetGlobalTimeUseCase
import com.practice.getup.domain.timer.GetIndicatorProgressValueUseCase
import com.practice.getup.domain.timer.GetLocalTimeUseCase
import com.practice.getup.domain.timer.GetStageListUseCase
import com.practice.getup.domain.timer.GetTimerStageUseCase
import com.practice.getup.domain.timer.GetWorkoutStagePositionUseCase
import com.practice.getup.domain.timer.PauseTimerUseCase
import com.practice.getup.domain.timer.PrepareTimerUseCase
import com.practice.getup.domain.timer.RestartTimerUseCase
import com.practice.getup.domain.timer.StartTimerUseCase
import com.practice.getup.presentation.timer.models.Stage
import com.practice.getup.presentation.timer.models.TimerStages
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TimerViewModel(
    private val workout: Workout,
    private val getGlobalTimeUseCase: GetGlobalTimeUseCase,
    private val getIndicatorProgressValueUseCase: GetIndicatorProgressValueUseCase,
    private val getLocalTimeUseCase: GetLocalTimeUseCase,
    private val getStageListUseCase: GetStageListUseCase,
    private val getTimerStageUseCase: GetTimerStageUseCase,
    private val getWorkoutStagePositionUseCase: GetWorkoutStagePositionUseCase,
    private val pauseTimerUseCase: PauseTimerUseCase,
    private val prepareTimerUseCase: PrepareTimerUseCase,
    private val restartTimerUseCase: RestartTimerUseCase,
    private val startTimerUseCase: StartTimerUseCase

) : ViewModel() {

    private val _timerStage = MutableLiveData(TimerStages.PREPARATION)
    val timerStage: LiveData<TimerStages> = _timerStage

    private val _localTimeToShow = MutableLiveData("")
    val localTimeToShow: LiveData<String> = _localTimeToShow

    private val _globalTimeToShow = MutableLiveData("")
    val globalTimeToShow: LiveData<String> = _globalTimeToShow

    private val _indicatorProgressValue = MutableLiveData(0)
    val indicatorProgressValue: LiveData<Int> = _indicatorProgressValue

    private val _stageList = MutableLiveData<MutableList<Stage>>(mutableListOf(Stage.DEFAULT))
    val stageList: LiveData<MutableList<Stage>> = _stageList

    private val _workoutStagePosition = MutableLiveData<Int>()
    val workoutStagePosition: LiveData<Int> = _workoutStagePosition

    private var isFragmentJustCreated = true


    init {

        prepareTimerUseCase.execute(workout)

        viewModelScope.launch {
            getTimerStageUseCase.execute().collectLatest {
                _timerStage.postValue(it)
            }
        }

        viewModelScope.launch {
            getLocalTimeUseCase.execute().collectLatest {
                _localTimeToShow.postValue(it)
            }
        }

        viewModelScope.launch {
            getGlobalTimeUseCase.execute().collectLatest {
                _globalTimeToShow.postValue(it)
            }
        }


        viewModelScope.launch {
            getIndicatorProgressValueUseCase.execute().collectLatest {
                _indicatorProgressValue.postValue(it)
            }
        }

        viewModelScope.launch {
            getStageListUseCase.execute().collectLatest {
                _stageList.value = it
            }
        }

        viewModelScope.launch {
            getWorkoutStagePositionUseCase.execute().collectLatest {
                _workoutStagePosition.postValue(it)
            }
        }

    }

    fun startTimer() = startTimerUseCase.execute()

    fun pauseTimer() = pauseTimerUseCase.execute()

    fun restartTimer() = restartTimerUseCase.execute()

    fun prepareTimer() {
        if (isFragmentJustCreated) {
            prepareTimerUseCase.execute(workout)
        }
        isFragmentJustCreated = false
    }
}

