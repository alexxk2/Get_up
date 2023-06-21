package com.practice.getup.data.repositories.impl

import com.practice.getup.data.settings.LocalSettings
import com.practice.getup.domain.repositories.SettingsRepository

class SettingsRepositoryImpl(private val localSettings: LocalSettings): SettingsRepository {

    override fun switchTheme(isNightMode: Boolean) {
        localSettings.switchTheme(isNightMode)
    }

    override fun switchLanguage(chosenLanguage: String) {
        localSettings.switchLanguage(chosenLanguage)
    }

    override fun getCurrentTheme(): Boolean = localSettings.getCurrentTheme()

    override fun getCurrentLanguage(): String = localSettings.getCurrentLanguage()

    override fun setLanguage() {
        localSettings.setLanguage()
    }

    override fun setNightMode() {
        localSettings.setNightMode()
    }
}