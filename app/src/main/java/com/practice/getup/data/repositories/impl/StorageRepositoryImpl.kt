package com.practice.getup.data.repositories.impl

import com.practice.getup.data.db.RoomStorage
import com.practice.getup.data.db.dto.WorkoutDto
import com.practice.getup.database.Workout
import com.practice.getup.domain.repositories.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StorageRepositoryImpl(private val roomStorage: RoomStorage): StorageRepository {

    override suspend fun addNewWorkout(workout: Workout) {
        val workoutDto = mapToData(workout)
        roomStorage.addNewWorkout(workoutDto)
    }

    override suspend fun deleteAllWorkouts() {
        roomStorage.deleteAllWorkouts()
    }

    override suspend fun deleteWorkout(workout: Workout) {
        val workoutDto = mapToData(workout)
        roomStorage.deleteWorkout(workoutDto)
    }

    override fun getAllWorkouts(): Flow<List<Workout>> {
        val resultFromData = roomStorage.getAllWorkouts()

        return resultFromData.map {

            it.map { workoutDto ->
                mapToDomain(workoutDto)
            }

        }
    }

    override suspend fun getWorkout(id: Int): Workout {
        val resultFromData = roomStorage.getWorkout(id)
        return  mapToDomain(resultFromData)

    }

    override suspend fun updateWorkout(workout: Workout) {
        val workoutDto = mapToData(workout)
        roomStorage.updateWorkout(workoutDto)
    }

    private fun mapToData(workout: Workout): WorkoutDto {
        with(workout) {
            return WorkoutDto(
                id = id,
                name = name,
                preparingTime = preparingTime,
                workTime = workTime,
                restTime = restTime,
                numberOfSets = numberOfSets
            )
        }
    }

    private fun mapToDomain(workoutDto: WorkoutDto): Workout{

        with(workoutDto) {
            return Workout(
                id = id,
                name = name,
                preparingTime = preparingTime,
                workTime = workTime,
                restTime = restTime,
                numberOfSets = numberOfSets
            )
        }
    }
}