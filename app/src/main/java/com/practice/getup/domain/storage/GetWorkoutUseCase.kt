package com.practice.getup.domain.storage

import com.practice.getup.database.Workout
import com.practice.getup.domain.repositories.StorageRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(id: Int): Workout = storageRepository.getWorkout(id)
}