package com.practice.getup.model

import com.practice.getup.activities.UiText

data class Stage(
    val id: Int,
    val stageName: UiText,
    val setsLeft: UiText,
    val hasFocus: Boolean
)
