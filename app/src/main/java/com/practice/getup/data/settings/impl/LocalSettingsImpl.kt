package com.practice.getup.data.settings.impl

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.practice.getup.data.settings.LocalSettings
import java.util.*

class LocalSettingsImpl(private val context: Context) : LocalSettings {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Application.MODE_PRIVATE)
    private var darkTheme = false
    private var language: String? = "en"


    override fun switchTheme(isDarkThemeEnabled: Boolean) {
        darkTheme = isDarkThemeEnabled

        sharedPrefs.edit()
            .putBoolean(IS_DARK_THEME, darkTheme)
            .apply()

        setNightMode(isDarkThemeEnabled)
    }

    override fun switchLanguage(chosenLanguage: String) {
        language = chosenLanguage

        sharedPrefs.edit()
            .putString(LANGUAGE, chosenLanguage)
            .apply()

        setLanguage(chosenLanguage!!)
    }

    override fun getCurrentTheme(): Boolean = sharedPrefs.getBoolean(IS_DARK_THEME, false)


    override fun getCurrentLanguage(): String = sharedPrefs.getString(LANGUAGE, "en")!!

    override fun setLanguage() {
        val isDarkTheme = sharedPrefs.getBoolean(IS_DARK_THEME, false)
        setNightMode(isDarkTheme)
    }

    override fun setNightMode() {
        val currentLanguage = sharedPrefs.getString(LANGUAGE, "en")!!
        setLanguage(currentLanguage)
    }


    private fun setNightMode(isDarkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun setLanguage(languageChoice: String) {
        val locale = Locale(languageChoice)
        val appLocale2 = LocaleListCompat.create(locale)
        // Call this on the main thread as it may require Activity.restart()
        AppCompatDelegate.setApplicationLocales(appLocale2)
    }

    companion object{
        const val SHARED_PREFS = "shared_prefs"
        const val IS_DARK_THEME = "is_dark_theme"
        const val LANGUAGE = "language"
    }

}