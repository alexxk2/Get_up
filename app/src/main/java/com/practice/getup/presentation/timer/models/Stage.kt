package com.practice.getup.presentation.timer.models


data class Stage(
    val id: String,
    val stageName: String,
    val setsLeft: String,
    val hasFocus: Boolean
)
{
    companion object{
        val DEFAULT = Stage(
            id = "default",
            stageName = "default",
            setsLeft = "default",
            hasFocus = false
        )
    }
}

