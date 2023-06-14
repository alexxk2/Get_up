package com.practice.getup.data.db

import com.practice.getup.data.db.dto.WorkoutDto

import kotlinx.coroutines.flow.Flow

interface RoomStorage {

    suspend fun addNewWorkout(workoutDto: WorkoutDto)

    suspend fun deleteAllWorkouts()

    suspend fun deleteWorkout(workoutDto: WorkoutDto)

    fun getAllWorkouts(): Flow<List<WorkoutDto>>

    suspend fun getWorkout(id: Int): WorkoutDto

    suspend fun updateWorkout(workoutDto: WorkoutDto)
}