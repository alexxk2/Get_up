package com.practice.getup.data.db.dto

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "workout")
data class WorkoutDto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val name: String,
    @ColumnInfo("preparing_time") val preparingTime: Int,
    @ColumnInfo("work_time") val workTime: Int,
    @ColumnInfo("rest_time") val restTime: Int,
    @ColumnInfo("number_of_sets") val numberOfSets: Int
): Parcelable
{
    companion object {

        val DEFAULT = WorkoutDto(
            name = "default",
            preparingTime = 10,
            workTime = 10,
            restTime = 10,
            numberOfSets = 5
        )
    }

}
