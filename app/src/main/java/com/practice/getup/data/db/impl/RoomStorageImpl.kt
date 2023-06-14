package com.practice.getup.data.db.impl

import android.content.Context
import com.practice.getup.app.App
import com.practice.getup.data.db.RoomStorage
import com.practice.getup.data.db.WorkoutDao
import com.practice.getup.data.db.dto.WorkoutDto
import kotlinx.coroutines.flow.Flow

class RoomStorageImpl(context: Context) : RoomStorage {

    private val workoutDao: WorkoutDao = (context as App).workoutDatabase.workoutDao()


    override suspend fun addNewWorkout(workoutDto: WorkoutDto) {
        workoutDao.insert(workoutDto)
    }

    override suspend fun deleteAllWorkouts() {
        workoutDao.deleteAll()
    }

    override suspend fun deleteWorkout(workoutDto: WorkoutDto) {
        workoutDao.delete(workoutDto)
    }

    override fun getAllWorkouts(): Flow<List<WorkoutDto>> = workoutDao.getAll()

    override suspend fun getWorkout(id: Int): WorkoutDto  = workoutDao.getWorkout(id)

    override suspend fun updateWorkout(workoutDto: WorkoutDto) {
        workoutDao.update(workoutDto)
    }
}