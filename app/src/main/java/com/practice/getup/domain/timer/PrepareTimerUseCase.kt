package com.practice.getup.domain.timer

import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.repositories.TimerRepository

class PrepareTimerUseCase(private val timerRepository: TimerRepository) {

    fun execute(workout: Workout) = timerRepository.prepareTimer(workout)
}