package com.practice.getup.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.getup.model.Options

class MainMenuViewModel: ViewModel() {

    private val _options = MutableLiveData(Options.DEFAULT)
    val options: LiveData<Options> = _options

    fun setOptions(optionsImport: Options){
        _options.value = optionsImport
    }

}