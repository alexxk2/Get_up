package com.practice.getup.domain.repositories

import com.practice.getup.database.Workout
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    suspend fun addNewWorkout(workout: Workout)

    suspend fun deleteAllWorkouts()

    suspend fun deleteWorkout(workout: Workout)

    fun getAllWorkouts(): Flow<List<Workout>>

    suspend fun getWorkout(id: Int): Workout

    suspend fun updateWorkout(workout: Workout)
}