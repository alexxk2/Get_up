package com.practice.getup.domain.storage

import com.practice.getup.database.Workout
import com.practice.getup.domain.repositories.StorageRepository

class UpdateWorkoutUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(workout: Workout) = storageRepository.updateWorkout(workout)
}