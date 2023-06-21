package com.practice.getup.data.settings

interface LocalSettings {

    fun switchTheme(isDarkThemeEnabled: Boolean)
    fun switchLanguage(chosenLanguage: String)
    fun getCurrentTheme(): Boolean
    fun getCurrentLanguage(): String
    fun setLanguage()
    fun setNightMode()
}