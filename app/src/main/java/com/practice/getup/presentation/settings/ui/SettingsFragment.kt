package com.practice.getup.presentation.settings.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.practice.getup.databinding.FragmentSettingsBinding
import com.practice.getup.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDarkThemeSwitcher()
        setLanguageChip()

        with(binding){
            darkThemeSwitcher.setOnCheckedChangeListener { _, isChecked ->
                viewModel.switchTheme(isChecked)
            }

            chipEnglish.setOnClickListener {
                viewModel.switchLanguage("en")
            }

            chipRussian.setOnClickListener {
                viewModel.switchLanguage("ru")
            }

            backButton.setOnClickListener { findNavController().navigateUp() }
        }

    }

    private fun setDarkThemeSwitcher(){
        binding.darkThemeSwitcher.isChecked = viewModel.getCurrentTheme()
    }

    private fun setLanguageChip(){
        when(viewModel.getCurrentLanguage()){
            "en" ->{
                binding.chipRussian.isChecked = false
                binding.chipEnglish.isChecked = true
            }
            else -> {
                binding.chipRussian.isChecked = true
                binding.chipEnglish.isChecked = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}