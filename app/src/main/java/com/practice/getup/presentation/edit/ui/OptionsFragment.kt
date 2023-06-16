package com.practice.getup.presentation.edit.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practice.getup.R
import com.practice.getup.databinding.FragmentOptionsBinding
import com.practice.getup.domain.models.Workout
import com.practice.getup.presentation.edit.view_model.OptionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OptionsViewModel by viewModel()
    private var id = 0
    private lateinit var workout: Workout
    private var titleSnackbar: Snackbar? = null
    private var dataSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
                id = it.getInt(ID)
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

        changeAppearance()

        viewModel.totalTime.observe(viewLifecycleOwner) { newTotalTime ->
            binding.totalWorkoutTime.text = newTotalTime
        }

        if (id > 0) {
            viewModel.retrieveWorkout(id).observe(viewLifecycleOwner) { currentWorkout ->
                workout = currentWorkout
                bind(workout)
            }
        } else binding.optionsAddUpdateButton.setOnClickListener { addNewWorkout() }


        binding.optionsBackButton.setOnClickListener { findNavController().navigateUp() }

        binding.editPreparationTime.addTextChangedListener {
            viewModel.getPreparationTimeInput(it)
            viewModel.calculateTotalTime()
            if (it.toString().toIntOrNull() == 0)
                binding.inputPreparationTime.error = showZeroInputErrorTime(it)
            else binding.inputPreparationTime.error = null
        }

        binding.editSetsNumber.addTextChangedListener {
            viewModel.getSetsNumberInput(it)
            viewModel.calculateTotalTime()
            if (it.toString().toIntOrNull() == 0)
                binding.inputSetsNumber.error = showZeroInputErrorTime(it)
            else binding.inputSetsNumber.error = null
        }

        binding.editWorkTime.addTextChangedListener {
            viewModel.getWorkTimeInput(it)
            viewModel.calculateTotalTime()
            if (it.toString().toIntOrNull() == 0)
                binding.inputWorkTime.error = showZeroInputErrorTime(it)
            else binding.inputWorkTime.error = null
        }

        binding.editRestTime.addTextChangedListener {
            viewModel.getRestTimeInput(it)
            viewModel.calculateTotalTime()
            if (it.toString().toIntOrNull() == 0)
                binding.inputRestTime.error = showZeroInputErrorTime(it)
            else binding.inputRestTime.error = null
        }

    }

    private fun isNumberInputValid(): Boolean{
        return viewModel.isNumberInputValid(
            binding.editPreparationTime.text.toString(),
            binding.editWorkTime.text.toString(),
            binding.editRestTime.text.toString(),
            binding.editSetsNumber.text.toString()
        )
    }

    private fun isNameInputValid(): Boolean{
        return viewModel.isNameInputValid(binding.editWorkoutName.text.toString())
    }

    private fun addNewWorkout() {
        if (isNumberInputValid() && isNameInputValid()) {
            viewModel.addNewWorkout(
                binding.editWorkoutName.text.toString(),
                binding.editPreparationTime.text.toString(),
                binding.editWorkTime.text.toString(),
                binding.editRestTime.text.toString(),
                binding.editSetsNumber.text.toString()
            )
            val action = OptionsFragmentDirections.actionOptionsFragmentToMainFragment()
            findNavController().navigate(action)
        } else if (!isNameInputValid()) {
            showTitleInputException()
        } else showWorkoutDataInputException()
    }

    private fun updateWorkout(){
        if (isNumberInputValid() && isNameInputValid()) {
            viewModel.updateWorkout(
                id,
                binding.editWorkoutName.text.toString(),
                binding.editPreparationTime.text.toString(),
                binding.editWorkTime.text.toString(),
                binding.editRestTime.text.toString(),
                binding.editSetsNumber.text.toString()
            )
            val action = OptionsFragmentDirections.actionOptionsFragmentToMainFragment()
            findNavController().navigate(action)
        } else if (!isNameInputValid()) {
            showTitleInputException()
        } else showWorkoutDataInputException()
    }

    private fun showWorkoutDataInputException() {
        dataSnackbar = makeSnackbar(getString(R.string.no_input_message))
        dataSnackbar?.show()
        hideKeyboard(binding.optionsActivity)
    }

    private fun showTitleInputException() {
        titleSnackbar = makeSnackbar(getString(R.string.no_input_name_message))
        titleSnackbar?.show()
        hideKeyboard(binding.optionsActivity)
    }

    private fun makeSnackbar(text: CharSequence): Snackbar {
        return Snackbar.make(binding.optionsActivity, text, 20000)
            .setAction(R.string.snackbar_ok_button) {}
            .setActionTextColor(resources.getColor(R.color.dark_blue, null))
            .setBackgroundTint(resources.getColor(R.color.super_soft_blue, null))
            .setTextColor(resources.getColor(R.color.black, null))
    }

    private fun changeAppearance(){
        if (id <= 0){
            binding.optionsDeleteButton.visibility = View.GONE
        }
        else  binding.optionsAddUpdateButton.text = getString(R.string.update)
    }

    private fun bind(workout: Workout){
        binding.apply {
            editWorkoutName.setText(workout.name, TextView.BufferType.SPANNABLE)
            editPreparationTime.setText(workout.preparingTime.toString(),TextView.BufferType.SPANNABLE)
            editSetsNumber.setText(workout.numberOfSets.toString(), TextView.BufferType.SPANNABLE)
            editWorkTime.setText(workout.workTime.toString(), TextView.BufferType.SPANNABLE)
            editRestTime.setText(workout.restTime.toString(), TextView.BufferType.SPANNABLE)

            optionsDeleteButton.setOnClickListener { showDeleteConfirmationDialog() }
            optionsAddUpdateButton.setOnClickListener { updateWorkout() }
        }

    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_message_delete))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.answer_no)) { _, _ -> }
            .setPositiveButton(getString(R.string.answer_yes)) { _, _ ->
                deleteWorkout()
            }
            .show()
    }

    private fun deleteWorkout(){
        viewModel.deleteWorkout(workout)
        findNavController().navigateUp()
    }

    private fun showZeroInputErrorTime(input: Editable?): String? {
        val convertedInput = input.toString().toIntOrNull()
        return if (convertedInput == 0) getString(R.string.non_null) else null
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        titleSnackbar?.dismiss()
        dataSnackbar?.dismiss()
        _binding = null
    }

    companion object {
        const val ID = "id"
    }
}