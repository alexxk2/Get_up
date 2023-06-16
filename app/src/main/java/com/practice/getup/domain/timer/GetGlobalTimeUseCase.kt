package com.practice.getup.domain.timer

import com.practice.getup.domain.repositories.TimerRepository
import kotlinx.coroutines.flow.Flow

class GetGlobalTimeUseCase(private val timerRepository: TimerRepository) {

    fun execute(): Flow<String> = timerRepository.getGlobalTime()
}