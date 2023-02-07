package com.practice.getup.activities

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.getup.ViewModels.WorkoutViewModel

class ViewModelFactory(private val workoutActivity: WorkoutActivity) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val viewModel: WorkoutViewModel = when (modelClass) {
            WorkoutViewModel::class.java -> WorkoutViewModel(workoutActivity.options)
            else -> throw java.lang.IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}
