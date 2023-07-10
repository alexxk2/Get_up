package com.practice.getup.data.timer.dto


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

