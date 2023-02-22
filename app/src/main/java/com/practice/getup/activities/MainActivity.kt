package com.practice.getup.activities

import android.app.ActivityManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.practice.getup.ViewModels.WorkoutViewModel
import com.practice.getup.databinding.ActivityMainBinding
import com.practice.getup.model.Options


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var options: Options
    private lateinit var launcherOptions: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT

        binding.testTextView0.text = options.preparingTime.toString()
        binding.testTextView1.text = options.workTime.toString()
        binding.testTextView2.text = options.restTime.toString()
        binding.testTextView3.text = options.numberOfSets.toString()
        binding.testTextView4.text = this.getString(options.exerciseType)

        launcherOptions =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

                if (result.resultCode == RESULT_OK) {
                    options = result.data?.getParcelableExtra(BACK_OPTIONS)!!
                    binding.testTextView0.text = options.preparingTime.toString()
                    binding.testTextView1.text = options.workTime.toString()
                    binding.testTextView2.text = options.restTime.toString()
                    binding.testTextView3.text = options.numberOfSets.toString()
                    binding.testTextView4.text = this.getString(options.exerciseType)
                }
            }

        binding.buttonSettings.setOnClickListener { onOptionsClick() }

        binding.buttonWatchList.setOnClickListener { onListClick() }

        binding.buttonStart.setOnClickListener { onStartClick() }



    }


    private fun onOptionsClick() {
        launcherOptions.launch(Intent(this, OptionsActivity::class.java))

    }

    private fun onListClick() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    private fun onStartClick(){
        val intent = Intent(this, WorkoutActivity::class.java)
        intent.putExtra(KEY_OPTIONS, options)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private companion object {
        const val KEY_OPTIONS = "OPTIONS"
        const val BACK_OPTIONS = "BACK OPTIONS"
    }

}