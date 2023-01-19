package com.practice.getup.model

import android.os.Parcelable
import com.practice.getup.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(
    val preparingTime: Int,
    val workTime: Int,
    val restTime: Int,
    val numberOfSets: Int,
    val exerciseType: Int
) : Parcelable {
    companion object {
        @JvmStatic val DEFAULT = Options(10,30, 60,5, R.string.exercise2)
    }
}


