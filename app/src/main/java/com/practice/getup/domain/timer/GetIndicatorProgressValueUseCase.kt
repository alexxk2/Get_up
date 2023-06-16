package com.practice.getup.domain.timer

import com.practice.getup.domain.repositories.TimerRepository
import kotlinx.coroutines.flow.Flow

class GetIndicatorProgressValueUseCase(private val timerRepository: TimerRepository) {

    fun execute(): Flow<Int> = timerRepository.getIndicatorProgressValue()
}