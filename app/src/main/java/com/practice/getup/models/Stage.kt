package com.practice.getup.models

import com.practice.getup.utils.UiText

data class Stage(
    val id: String,
    val stageName: UiText,
    val setsLeft: UiText,
    val hasFocus: Boolean
)
