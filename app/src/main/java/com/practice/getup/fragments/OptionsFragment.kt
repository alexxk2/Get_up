package com.practice.getup.fragments

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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.practice.getup.App
import com.practice.getup.R
import com.practice.getup.activities.ViewModelFactoryFragments
import com.practice.getup.database.Workout
import com.practice.getup.databinding.FragmentOptionsBinding
import com.practice.getup.model.Options
import com.practice.getup.viewModels.OptionsViewModel
import com.practice.getup.viewModels.WorkoutDatabaseViewModel
import com.practice.getup.viewModels.WorkoutDatabaseViewModelFactory


class OptionsFragment : Fragment() {

    private var _binding: FragmentOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var options: Options
    private val viewModel: OptionsViewModel by viewModels{ViewModelFactoryFragments(options)}
    private var id = 0
    private lateinit var workout: Workout


    private val databaseViewModel: WorkoutDatabaseViewModel by activityViewModels {
        WorkoutDatabaseViewModelFactory(
            (activity?.application as App).workoutDatabase.workoutDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
                options  = it.getParcelable(OPTIONS)?: Options.DEFAULT
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

        //NEW CODE

        changeAppearance()

        viewModel.totalTime.observe(viewLifecycleOwner) { newTotalTime ->
            binding.totalWorkoutTime.text = newTotalTime
        }

        if (id > 0) {
            databaseViewModel.retrieveWorkout(id).observe(viewLifecycleOwner) { currentWorkout ->
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

        //OLD CODE

        /*viewModel.totalTime.observe(viewLifecycleOwner) { newTotalTime ->
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
        }*/

    }

    //NEW CODE
    private fun isNumberInputValid(): Boolean{
        return databaseViewModel.isNumberInputValid(
            binding.editPreparationTime.text.toString(),
            binding.editWorkTime.text.toString(),
            binding.editRestTime.text.toString(),
            binding.editSetsNumber.text.toString()
        )
    }

    private fun isNameInputValid(): Boolean{
        return databaseViewModel.isNameInputValid(binding.editWorkoutName.text.toString())
    }

    private fun addNewWorkout() {
        if (isNumberInputValid() && isNameInputValid()) {
            databaseViewModel.addNewWorkout(
                binding.editWorkoutName.text.toString(),
                binding.editPreparationTime.text.toString(),
                binding.editWorkTime.text.toString(),
                binding.editRestTime.text.toString(),
                binding.editSetsNumber.text.toString()
            )
            val action = OptionsFragmentDirections.actionOptionsFragmentToMainFragment()
            findNavController().navigate(action)
        } else if (!isNameInputValid()) {
            showTextInputException()
        } else showNumberInputException()
    }

    private fun updateWorkout(){
        if (isNumberInputValid() && isNameInputValid()) {
            databaseViewModel.updateWorkout(
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
            showTextInputException()
        } else showNumberInputException()
    }

    private fun showNumberInputException() {
        Snackbar.make(binding.optionsActivity, getString(R.string.no_input_message), 20000)
            .setAction(R.string.snackbar_ok_button) {}
            .show()
        hideKeyboard(binding.optionsActivity)
    }

    private fun showTextInputException() {
        Snackbar.make(binding.optionsActivity, getString(R.string.no_input_name_message), 20000)
            .setAction(R.string.snackbar_ok_button) {}
            .show()
        hideKeyboard(binding.optionsActivity)
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
            //добавить клик листенер на апдейт объекта
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
        databaseViewModel.deleteWorkout(workout)
        findNavController().navigateUp()
    }

    //OLD CODE
    private fun showZeroInputErrorTime(input: Editable?): String? {
        val convertedInput = input.toString().toIntOrNull()
        return if (convertedInput == 0) getString(R.string.non_null) else null
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
            saveOptions()
            val action = OptionsFragmentDirections.actionOptionsFragmentToMainFragment()
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

    private fun saveOptions(){
        val sharedPref = activity?.getSharedPreferences(SHARED_PREF,0)
        val jSonOptions  = Gson().toJson(viewModel.options.value)
        sharedPref?.edit()?.putString(SAVED_OPTIONS,jSonOptions)?.apply()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val OPTIONS = "options"
        const val SAVED_OPTIONS = "saved_options"
        const val SHARED_PREF = "shared_preferences"
        const val ID = "id"
    }
}