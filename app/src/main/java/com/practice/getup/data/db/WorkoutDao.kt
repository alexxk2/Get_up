package com.practice.getup.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practice.getup.data.db.dto.WorkoutDto
import kotlinx.coroutines.flow.Flow


@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: WorkoutDto)

    @Update
    suspend fun update(workout: WorkoutDto)

    @Delete
    suspend fun delete(workout: WorkoutDto)

    @Query("SELECT * FROM workout ORDER BY name ASC")
    fun getAll(): Flow<List<WorkoutDto>>

    @Query("SELECT * FROM workout WHERE id =:id")
    suspend fun getWorkout(id: Int): WorkoutDto

    @Query("DELETE FROM workout")
    suspend fun deleteAll()

}