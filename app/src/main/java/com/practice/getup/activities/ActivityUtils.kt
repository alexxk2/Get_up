package com.practice.getup.activities


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.getup.database.Workout
import com.practice.getup.database.WorkoutDao
import com.practice.getup.model.Options
import com.practice.getup.viewModels.OptionsViewModel
import com.practice.getup.viewModels.WorkoutDatabaseViewModel
import com.practice.getup.viewModels.WorkoutViewModel
import java.lang.IllegalArgumentException


class ViewModelFactoryFragments(private val workout: Workout) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(workout) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }


}




