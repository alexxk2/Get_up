package com.practice.getup.domain.settings

import com.practice.getup.domain.repositories.SettingsRepository

class GetCurrentThemeUseCase(private val settingsRepository: SettingsRepository) {

    fun execute() = settingsRepository.getCurrentTheme()
}