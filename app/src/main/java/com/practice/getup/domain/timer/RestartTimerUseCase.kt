package com.practice.getup.domain.timer

import com.practice.getup.domain.repositories.TimerRepository

class RestartTimerUseCase(private val timerRepository: TimerRepository) {

    fun execute() = timerRepository.restartTimer()
}