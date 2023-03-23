package com.practice.getup.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.practice.getup.R
import com.practice.getup.activities.OptionsActivity.Companion.BACK_OPTIONS
import com.practice.getup.activities.ViewModelFactoryForFragments
import com.practice.getup.databinding.FragmentOptionsBinding
import com.practice.getup.model.Options
import com.practice.getup.viewModels.OptionsViewModel


class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var options: Options
    private val viewModel: OptionsViewModel by viewModels{ViewModelFactoryForFragments(options)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
                options  = it.getParcelable(OPTIONS)?: Options.DEFAULT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOptionsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.totalTime.observe(viewLifecycleOwner) { newTotalTime ->
            binding.totalWorkoutTime.text = newTotalTime
        }

        with(binding) {
            optionsSaveButton.setOnClickListener {
                setOptions()
            }


            editPreparationTime.addTextChangedListener {
                with(viewModel) {
                    getPreparationTimeInput(it)
                    calculateTotalTime()
                }

                inputPreparationTime.error = showZeroInputErrorTime(it)
            }

            editWorkTime.addTextChangedListener {
                with(viewModel) {
                    getWorkTimeInput(it)
                    calculateTotalTime()
                }

                inputWorkTime.error = showZeroInputErrorTime(it)
            }

            editRestTime.addTextChangedListener {
                with(viewModel) {
                    getRestTimeInput(it)
                    calculateTotalTime()
                }

                inputRestTime.error = showZeroInputErrorTime(it)

            }

            editSetsNumber.addTextChangedListener {
                with(viewModel) {
                    getSetsNumberInput(it)
                    calculateTotalTime()
                }

                inputSetsNumber.error = showZeroInputErrorSets(it)
            }

            optionsBackButton.setOnClickListener {

                binding.root.findNavController().navigateUp()
            }
        }

    }

    private fun showZeroInputErrorTime(input: Editable?): String? {
        val convertedInput = input.toString().toIntOrNull()
        return if (convertedInput == 0) getString(R.string.number_format_exception_rest_short) else null
    }

    private fun showZeroInputErrorSets(input: Editable?): String? {
        val convertedInput = input.toString().toIntOrNull()
        return if (convertedInput == 0) getString(R.string.number_format_exception_sets_short) else null
    }

    private fun setOptions() {

        viewModel.updateOptions()

        with(viewModel.options) {
            val preparationTimeInput = value?.preparingTime ?: Options.DEFAULT.preparingTime
            val workTimeInput = value?.workTime ?: Options.DEFAULT.workTime
            val restTimeInput = value?.restTime ?: Options.DEFAULT.restTime
            val numberOfSets = value?.numberOfSets ?: Options.DEFAULT.numberOfSets

            if (preparationTimeInput == 0) {
                showZeroException(R.string.number_format_exception_preparation)
                return
            }
            if (workTimeInput == 0) {
                showZeroException(R.string.number_format_exception_work)
                return
            }
            if (restTimeInput == 0) {
                showZeroException(R.string.number_format_exception_rest)
                return
            }
            if (numberOfSets == 0) {
                showZeroException(R.string.number_format_exception_sets)
                return
            }

            hideKeyboard(binding.optionsActivity)

            val action = OptionsFragmentDirections.actionOptionsFragmentToMainFragment(viewModel.options.value?: Options.DEFAULT)
            binding.root.findNavController().navigate(action)

        }
    }

    private fun showZeroException(exceptionString: Int) {
        Snackbar.make(binding.optionsActivity, exceptionString, 20000)
            .setAction(R.string.snackbar_ok_button) {}
            .show()
        hideKeyboard(binding.optionsActivity)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val OPTIONS = "options"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}