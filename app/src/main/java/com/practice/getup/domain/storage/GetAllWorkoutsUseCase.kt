package com.practice.getup.domain.storage

import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.repositories.StorageRepository
import kotlinx.coroutines.flow.Flow

class GetAllWorkoutsUseCase(private val storageRepository: StorageRepository) {

    fun execute(): Flow<List<Workout>> = storageRepository.getAllWorkouts()
}