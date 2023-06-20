package com.practice.getup.domain.settings

import com.practice.getup.domain.repositories.SettingsRepository

class SwitchLanguageUseCase(private val settingsRepository: SettingsRepository) {

    fun execute(chosenLanguage: String){
        settingsRepository.switchLanguage(chosenLanguage)
    }
}