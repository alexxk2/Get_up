package com.practice.getup.data.repositories.impl


import com.practice.getup.data.db.dto.WorkoutDto
import com.practice.getup.data.timer.Timer
import com.practice.getup.data.timer.dto.StageDto
import com.practice.getup.data.timer.dto.TimerStagesDto
import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.repositories.TimerRepository
import com.practice.getup.presentation.timer.models.Stage
import com.practice.getup.presentation.timer.models.TimerStages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TimerRepositoryImpl(private val timer: Timer): TimerRepository {

    override fun prepareTimer(workout: Workout) {
        val workoutDto = mapToData(workout)
         timer.prepareTimer(workoutDto)
    }

    override fun getWorkoutStagePosition(): Flow<Int> = timer.getWorkoutStagePosition()


    override fun getGlobalTime(): Flow<String>  = timer.getGlobalTime()

    override fun getIndicatorProgressValue(): Flow<Int> = timer.getIndicatorProgressValue()

    override fun getLocalTime(): Flow<String>  = timer.getLocalTime()


    override fun getStageList(): Flow<MutableList<Stage>> {
        val dataResult = timer.getStageList()
        return dataResult.map {
            it.map { stageDto ->
                mapStagesToDomain(stageDto)
            }.toMutableList()
        }
    }

    override fun getTimerStage(): Flow<TimerStages> {
        val dataResult = timer.getTimerStage()
        return dataResult.map {
            mapTimerStagesToDomain(it)
        }
    }

    override fun pauseTimer()  = timer.pauseTimer()

    override fun restartTimer()  = timer.restartTimer()

    override fun startTimer()  = timer.startTimer()


    private fun mapToData(workout: Workout): WorkoutDto {

        return with(workout) {
            WorkoutDto(
                id = id,
                name = name,
                preparingTime = preparingTime,
                workTime = workTime,
                restTime = restTime,
                numberOfSets = numberOfSets
            )
        }
    }

    private fun mapStagesToDomain(stageDto: StageDto): Stage =
        with(stageDto) {
            Stage(
                id = id,
                stageName = stageName,
                setsLeft = setsLeft,
                hasFocus = hasFocus
            )
        }


    private fun mapTimerStagesToDomain(timerStagesDto: TimerStagesDto): TimerStages =
        when(timerStagesDto){
            TimerStagesDto.PAUSE -> TimerStages.PAUSE
            TimerStagesDto.PREPARATION -> TimerStages.PREPARATION
            TimerStagesDto.RESUME -> TimerStages.RESUME
            TimerStagesDto.RESTART -> TimerStages.RESTART
        }


}