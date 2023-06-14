package com.practice.getup.presentation.timer.models

import com.practice.getup.presentation.timer.utils.UiText

data class Stage(
    val id: String,
    val stageName: UiText,
    val setsLeft: UiText,
    val hasFocus: Boolean
)
