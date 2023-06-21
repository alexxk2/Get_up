package com.practice.getup.domain.settings

import com.practice.getup.domain.repositories.SettingsRepository

class SetLanguageUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(){
        settingsRepository.setLanguage()
    }
}