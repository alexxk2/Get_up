package com.practice.getup.domain.timer

import com.practice.getup.domain.repositories.TimerRepository
import com.practice.getup.presentation.timer.models.TimerStages
import kotlinx.coroutines.flow.Flow

class GetTimerStageUseCase(private val timerRepository: TimerRepository) {

    fun execute(): Flow<TimerStages> = timerRepository.getTimerStage()
}