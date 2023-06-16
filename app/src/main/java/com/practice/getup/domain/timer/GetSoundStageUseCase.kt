package com.practice.getup.domain.timer

import com.practice.getup.domain.repositories.TimerRepository
import com.practice.getup.presentation.timer.models.SoundStages
import kotlinx.coroutines.flow.Flow

class GetSoundStageUseCase(private val timerRepository: TimerRepository) {

    fun execute():Flow<SoundStages> = timerRepository.getSoundStage()
}