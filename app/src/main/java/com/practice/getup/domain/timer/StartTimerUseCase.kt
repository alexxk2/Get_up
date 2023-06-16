package com.practice.getup.domain.timer

import com.practice.getup.domain.repositories.TimerRepository

class StartTimerUseCase(private val timerRepository: TimerRepository) {

    fun execute() = timerRepository.startTimer()
}