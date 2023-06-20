package com.practice.getup.domain.repositories


interface SettingsRepository {
    fun switchTheme(isNightMode: Boolean)
    fun switchLanguage(chosenLanguage: String)
    fun getCurrentTheme(): Boolean
    fun getCurrentLanguage(): String
    fun setLanguage()
    fun setNightMode()
}