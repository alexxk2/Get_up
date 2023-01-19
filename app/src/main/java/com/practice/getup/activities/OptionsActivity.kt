package com.practice.getup.activities
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.practice.getup.R
import com.practice.getup.databinding.ActivityOptionsBinding
import com.practice.getup.model.Options

class OptionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsBinding
    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOptionsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        options = Options.DEFAULT

        //биндинги, чтобы прочекать как присваиваются значния из эдит текста. Если юзать их,
        //то включи и в лэйауте
       /* binding.testTextView1.text = options.workTime.toString()
        binding.testTextView2.text = options.restTime.toString()
        binding.testTextView3.text = options.numberOfSets.toString()
        binding.testTextView4.text = this.getString(options.exerciseType)*/

        /*binding.optionsOkButton.setOnClickListener { sendOptionsBack() }*/

        with(binding) {
            optionsOkButton.setOnClickListener {
                setOptions()
            }
            totalWorkoutTime.text = calculateTotalTime()

            editPreparationTime.addTextChangedListener {
                binding.totalWorkoutTime.text = countTotalTime()

                val preparationTimeInput = it.toString().toIntOrNull()
                if (preparationTimeInput == 0) {
                    showZeroException(R.string.number_format_exception_preparation)
                }
            }

            editWorkTime.addTextChangedListener {
                binding.totalWorkoutTime.text = countTotalTime()

                val workTimeInput = it.toString().toIntOrNull()
                if (workTimeInput == 0) {
                    showZeroException(R.string.number_format_exception_work)
                }
            }

            editRestTime.addTextChangedListener {
                binding.totalWorkoutTime.text = countTotalTime()

                val restTimeInput = it.toString().toIntOrNull()
                if (restTimeInput == 0) {
                    showZeroException(R.string.number_format_exception_rest)
                }
            }

            editSetsNumber.addTextChangedListener {
                binding.totalWorkoutTime.text = countTotalTime()

                val numberOfSetsInput = it.toString().toIntOrNull()
                if (numberOfSetsInput == 0) {
                    showZeroException(R.string.number_format_exception_sets)
                }
            }
        }
    }

   /* override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }*/

    private fun setOptions(){

        val preparationTimeInput = binding.editPreparationTime.text.toString().toIntOrNull() ?: 10
        val workTimeInput = binding.editWorkTime.text.toString().toIntOrNull() ?: 30
        val restTimeInput = binding.editRestTime.text.toString().toIntOrNull() ?: 60
        val numberOfSets = binding.editSetsNumber.text.toString().toIntOrNull() ?: 5


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

        //биндинги, чтобы прочекать как присваиваются значния из эдит текста. Если юзать их,
        //то включи и в лэйауте
       /* binding.testTextView1.text = workTimeInput.toString()
        binding.testTextView2.text = restTimeInput.toString()
        binding.testTextView3.text = numberOfSets.toString()*/

        hideKeyboard(binding.optionsActivity)

        options = Options(preparationTimeInput, workTimeInput, restTimeInput, numberOfSets,
            R.string.exercise3
        )

        val backOptionsIntent = Intent()
        backOptionsIntent.putExtra(BACK_OPTIONS, options )
        setResult(Activity.RESULT_OK, backOptionsIntent)
        finish()
    }


    private fun showZeroException(exceptionString: Int){
        Snackbar.make(binding.optionsActivity, exceptionString, 20000)
            .setAction(R.string.snackbar_ok_button) {}
            .show()
        hideKeyboard(binding.optionsActivity)
    }


    private fun calculateTotalTime(preparationTime: Int = 10, workTime: Int = 30, restTime: Int = 60, numberOfSets: Int = 5): String{

        val totalSeconds = (workTime + restTime) * numberOfSets + preparationTime
        val secondsToShow = totalSeconds % 60
        val minutesToShow = (totalSeconds / 60) % 60
        val hoursToShow = totalSeconds / 3600

        return "$hoursToShow : $minutesToShow : $secondsToShow"

    }

    private fun countTotalTime(): String {

        val preparationTimeInput = binding.editPreparationTime.text.toString().toIntOrNull() ?: 10
        val workTimeInput = binding.editWorkTime.text.toString().toIntOrNull() ?: 30
        val restTimeInput = binding.editRestTime.text.toString().toIntOrNull() ?: 60
        val numberOfSetsInput = binding.editSetsNumber.text.toString().toIntOrNull() ?: 5

        return calculateTotalTime(preparationTimeInput, workTimeInput, restTimeInput, numberOfSetsInput)

    }

    //hide keyboard when Snackbar is shown. PS: view-parameter is a current Edit text shown
    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        const val BACK_OPTIONS = "BACK OPTIONS"
        private const val KEY_OPTIONS = "KEY_OPTIONS"
    }

}


