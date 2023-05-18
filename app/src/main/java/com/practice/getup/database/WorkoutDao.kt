package com.practice.getup.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: Workout)

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Query("SELECT * FROM workout ORDER BY name ASC")
    fun getAll(): Flow<List<Workout>>

    @Query("SELECT * FROM workout WHERE id =:id")
    fun getWorkout(id: Int): Flow<Workout>

    @Query("DELETE FROM workout")
    suspend fun deleteAll()

}