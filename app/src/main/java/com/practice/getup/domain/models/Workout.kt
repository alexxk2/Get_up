package com.practice.getup.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout(
    val id: Int = 0,
    val name: String,
    val preparingTime: Int,
    val workTime: Int,
    val restTime: Int,
    val numberOfSets: Int
): Parcelable
