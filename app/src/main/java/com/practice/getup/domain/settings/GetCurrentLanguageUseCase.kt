package com.practice.getup.domain.settings

import com.practice.getup.domain.repositories.SettingsRepository

class GetCurrentLanguageUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(): String = settingsRepository.getCurrentLanguage()
}