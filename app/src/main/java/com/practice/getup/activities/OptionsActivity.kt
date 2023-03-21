package com.practice.getup.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.practice.getup.R
import com.practice.getup.viewModels.OptionsViewModel
import com.practice.getup.databinding.ActivityOptionsBinding
import com.practice.getup.model.Options

class OptionsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityOptionsBinding
    private lateinit var options: Options
    private val viewModel: OptionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        options = Options.DEFAULT

        viewModel.totalTime.observe(this) { newTotalTime ->
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
                finish()
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

            val backOptionsIntent = Intent()
            backOptionsIntent.putExtra(BACK_OPTIONS, value)
            setResult(Activity.RESULT_OK, backOptionsIntent)
            finish()
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
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val BACK_OPTIONS = "BACK OPTIONS"
    }

}


