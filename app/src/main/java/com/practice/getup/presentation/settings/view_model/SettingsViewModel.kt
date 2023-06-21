package com.practice.getup.presentation.settings.view_model

import androidx.lifecycle.ViewModel
import com.practice.getup.domain.settings.GetCurrentLanguageUseCase
import com.practice.getup.domain.settings.GetCurrentThemeUseCase
import com.practice.getup.domain.settings.SwitchLanguageUseCase
import com.practice.getup.domain.settings.SwitchThemeUseCase

class SettingsViewModel(
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    private val getCurrentThemeUseCase: GetCurrentThemeUseCase,
    private val switchLanguageUseCase: SwitchLanguageUseCase,
    private val switchThemeUseCase: SwitchThemeUseCase
) : ViewModel() {


    fun getCurrentLanguage(): String = getCurrentLanguageUseCase.execute()

    fun getCurrentTheme(): Boolean = getCurrentThemeUseCase.execute()

    fun switchLanguage(chosenLanguage: String) = switchLanguageUseCase.execute(chosenLanguage)

    fun switchTheme(isNightMode: Boolean) = switchThemeUseCase.execute(isNightMode)


}