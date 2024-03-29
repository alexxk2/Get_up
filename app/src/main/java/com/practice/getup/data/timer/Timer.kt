package com.practice.getup.data.timer

import com.practice.getup.data.db.dto.WorkoutDto
import com.practice.getup.data.timer.dto.StageDto
import com.practice.getup.data.timer.dto.TimerStagesDto
import kotlinx.coroutines.flow.Flow

interface Timer {

    fun prepareTimer(workoutDto: WorkoutDto)
    fun getWorkoutStagePosition(): Flow<Int>
    fun getGlobalTime(): Flow<String>
    fun getIndicatorProgressValue(): Flow<Int>
    fun getLocalTime(): Flow<String>
    fun getStageList(): Flow<MutableList<StageDto>>
    fun getTimerStage(): Flow<TimerStagesDto>
    fun pauseTimer()
    fun restartTimer()
    fun startTimer()

}