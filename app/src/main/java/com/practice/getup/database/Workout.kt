package com.practice.getup.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout(
    val id: Int = 0,
    val name: String,
    val preparingTime: Int,
    val workTime: Int,
    val restTime: Int,
    val numberOfSets: Int
) : Parcelable

/*
@Parcelize
@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @NonNull @ColumnInfo val name: String,
    @NonNull @ColumnInfo("preparing_time") val preparingTime: Int,
    @NonNull @ColumnInfo("work_time") val workTime: Int,
    @NonNull @ColumnInfo("rest_time") val restTime: Int,
    @NonNull @ColumnInfo("number_of_sets") val numberOfSets: Int
): Parcelable*/
