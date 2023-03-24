package com.practice.getup.activities


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.getup.model.Options
import com.practice.getup.viewModels.OptionsViewModel
import com.practice.getup.viewModels.WorkoutViewModel



class ViewModelFactoryFragments(private val options: Options) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val viewModel: ViewModel = when (modelClass) {
            OptionsViewModel::class.java -> OptionsViewModel(options)
            WorkoutViewModel::class.java -> WorkoutViewModel(options)
            else -> throw java.lang.IllegalStateException("Unknown view model class")
        }

        return viewModel as T
    }
}




