package com.practice.getup.model

import com.practice.getup.activities.UiText

data class Stage(
    val stageName: UiText,
    val setsLeft: UiText,
    var hasFocus: Boolean
)
