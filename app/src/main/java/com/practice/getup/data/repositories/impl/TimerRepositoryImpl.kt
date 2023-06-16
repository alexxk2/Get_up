package com.practice.getup.data.repositories.impl


import com.practice.getup.data.db.dto.WorkoutDto
import com.practice.getup.data.timer.Timer
import com.practice.getup.data.timer.dto.SoundStagesDto
import com.practice.getup.data.timer.dto.StageDto
import com.practice.getup.data.timer.dto.TimerStagesDto
import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.repositories.TimerRepository
import com.practice.getup.presentation.timer.models.SoundStages
import com.practice.getup.presentation.timer.models.Stage
import com.practice.getup.presentation.timer.models.TimerStages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
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

    override fun getSoundStage(): Flow<SoundStages> {
        val dataResult = timer.getSoundStage()
        return dataResult.map {
            mapSoundStagesToDomain(it)
        }
    }

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

    private fun mapSoundStagesToDomain(soundStagesDto: SoundStagesDto): SoundStages =
        when (soundStagesDto) {
            SoundStagesDto.WORK -> SoundStages.WORK
            SoundStagesDto.REST -> SoundStages.REST
            SoundStagesDto.FINISH -> SoundStages.FINISH
            SoundStagesDto.SILENT -> SoundStages.SILENT
            SoundStagesDto.COUNTDOWN3 -> SoundStages.COUNTDOWN3
            SoundStagesDto.COUNTDOWN2 -> SoundStages.COUNTDOWN2
            SoundStagesDto.COUNTDOWN1 -> SoundStages.COUNTDOWN1
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