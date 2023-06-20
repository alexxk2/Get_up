package com.practice.getup.domain.settings

import com.practice.getup.domain.repositories.SettingsRepository

class SwitchThemeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(isNightMode: Boolean){
        settingsRepository.switchTheme(isNightMode)
    }
}