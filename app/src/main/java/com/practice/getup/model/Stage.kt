package com.practice.getup.model

import com.practice.getup.activities.UiText

data class Stage(
    val id: String,
    val stageName: UiText,
    val setsLeft: UiText,
    val hasFocus: Boolean
)
