package com.practice.getup.domain.storage

import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.repositories.StorageRepository

class GetWorkoutUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(id: Int): Workout = storageRepository.getWorkout(id)
}