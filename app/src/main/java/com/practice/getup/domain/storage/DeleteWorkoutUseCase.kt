package com.practice.getup.domain.storage

import com.practice.getup.database.Workout
import com.practice.getup.domain.repositories.StorageRepository

class DeleteWorkoutUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(workout: Workout) = storageRepository.deleteWorkout(workout)
}