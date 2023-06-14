package com.practice.getup.domain.storage

import com.practice.getup.domain.repositories.StorageRepository

class DeleteAllWorkoutsUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute() = storageRepository.deleteAllWorkouts()
}