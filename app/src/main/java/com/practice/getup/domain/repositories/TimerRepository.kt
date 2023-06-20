package com.practice.getup.domain.repositories

import com.practice.getup.domain.models.Workout
import com.practice.getup.presentation.timer.models.SoundStages
import com.practice.getup.presentation.timer.models.Stage
import com.practice.getup.presentation.timer.models.TimerStages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TimerRepository {

    fun prepareTimer(workout: Workout)
    fun getWorkoutStagePosition(): Flow<Int>
    fun getGlobalTime(): Flow<String>
    fun getIndicatorProgressValue(): Flow<Int>
    fun getLocalTime(): Flow<String>
    fun getStageList(): Flow<MutableList<Stage>>
    fun getTimerStage(): Flow<TimerStages>
    fun pauseTimer()
    fun restartTimer()
    fun startTimer()

}
