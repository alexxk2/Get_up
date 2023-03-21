package com.practice.getup.activities


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.getup.viewModels.WorkoutViewModel


class ViewModelFactory(private val workoutActivity: WorkoutActivity) : ViewModelProvider.Factory {

    companion object {
        const val OPTIONS = "OPTIONS"
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val viewModel: WorkoutViewModel = when (modelClass) {
            WorkoutViewModel::class.java -> WorkoutViewModel(workoutActivity.intent.getParcelableExtra(OPTIONS)!!)
            else -> throw java.lang.IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}




