package com.practice.getup.domain.timer

import com.practice.getup.domain.repositories.TimerRepository
import com.practice.getup.presentation.timer.models.Stage
import kotlinx.coroutines.flow.Flow


class GetStageListUseCase(private val timerRepository: TimerRepository) {

    fun execute(): Flow<MutableList<Stage>> = timerRepository.getStageList()
}