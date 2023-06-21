package com.practice.getup.domain.settings

import com.practice.getup.domain.repositories.SettingsRepository

class SetNightModeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(){
        settingsRepository.setNightMode()
    }
}