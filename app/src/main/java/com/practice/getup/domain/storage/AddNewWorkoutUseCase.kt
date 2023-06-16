package com.practice.getup.domain.storage

import com.practice.getup.domain.models.Workout
import com.practice.getup.domain.repositories.StorageRepository

class AddNewWorkoutUseCase(private val storageRepository: StorageRepository) {

    suspend fun execute(workout: Workout){
        storageRepository.addNewWorkout(workout)
    }
}