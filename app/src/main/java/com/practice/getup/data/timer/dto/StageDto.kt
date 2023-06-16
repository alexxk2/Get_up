package com.practice.getup.data.timer.dto

import com.practice.getup.presentation.timer.utils.UiText

data class StageDto(
    val id: String,
    val stageName: String,
    val setsLeft: String,
    val hasFocus: Boolean
){
    companion object{
        val DEFAULT = StageDto(
            id = "default",
            stageName = "default",
            setsLeft = "default",
            hasFocus = false
        )
    }
}

