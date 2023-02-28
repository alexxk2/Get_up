package com.practice.getup.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.practice.getup.R
import com.practice.getup.ViewModels.WorkoutViewModel
import com.practice.getup.adapters.WorkoutAdapter
import com.practice.getup.databinding.ActivityWorkoutBinding
import com.practice.getup.model.Stage
import com.practice.getup.model.TimerStages


class WorkoutActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var adapter: WorkoutAdapter


    private val viewModel: WorkoutViewModel by viewModels { ViewModelFactory(this) }
    //private lateinit var timer: SuperTimer


    //TODO не сохраняет текущее положение данных при повороте во всех активностях
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutBinding.inflate(layoutInflater).also { setContentView(it.root) }

        adapter = WorkoutAdapter(this, viewModel.stageList.value!!)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        val itemAnimator = binding.recyclerView.itemAnimator

        viewModel.currentStagePosition.value?.let { scrollToCurrentStage(it) }


        viewModel.timerStage.observe(this) { timerStage ->
            switchControlButton(timerStage)
        }

        viewModel.localTimeToShow.observe(this) { localTimeToShow ->
            binding.localTimerView.text = localTimeToShow
        }

        viewModel.globalTimeToShow.observe(this) { globalTimeToShow ->
            binding.generalTimerView.text = globalTimeToShow
        }

        viewModel.indicatorProgressValue.observe(this) { indicatorProgressValue ->
            binding.globalProgressIndicator.progress = indicatorProgressValue
        }

        viewModel.stageList.observe(this) { stageList ->

            adapter.dataSet = stageList

        }

        viewModel.currentStagePosition.observe(this) { currentStagePosition ->
            //scrollToCurrentStage(currentStagePosition)
        }

        binding.startButton.setOnClickListener { viewModel.startTimer() }
        binding.pauseButton.setOnClickListener { viewModel.pauseTimer() }
        //TODO изменить цвет нажатой клавиши - сейчас фиолетовый
        binding.restartButton.setOnClickListener { viewModel.restartTimer() }

    }

    private fun switchControlButton(timerStage: TimerStages) {
        when (timerStage) {
            TimerStages.PREPARATION -> {
                showPrepStageButtons()
            }
            TimerStages.RESUME -> {
                showResumeStageButtons()
            }
            TimerStages.PAUSE -> {
                showPauseStageButtons()
            }
            TimerStages.RESTART -> {
                showRestartStageButtons()
            }
        }
    }

    private fun scrollToCurrentStage(currentStagePosition: Int) {

        if (currentStagePosition >= 0) {
            binding.recyclerView.smoothScrollToPosition(currentStagePosition)
        }




    }
     private fun scrollToCurrentStage2() {

        viewModel.stageList.value?.forEachIndexed { index, it ->
            if (it.hasFocus)
                binding.recyclerView.smoothScrollToPosition(index)
        }
    }
    private fun getPosition(list: MutableList<Stage>): Int {
        var indexOfPosition = 0
       list.forEachIndexed { index, it ->
            if (it.hasFocus)
                indexOfPosition = index
        }

        return indexOfPosition
    }


    private fun showResumeStageButtons() {
        binding.pauseButton.animate().translationX(-175f)
        binding.startButton.animate().translationX(-175f)
        binding.restartButton.animate().translationX(175f)
        binding.pauseButton.visibility = View.VISIBLE
        binding.startButton.visibility = View.INVISIBLE
        binding.restartButton.visibility = View.VISIBLE
    }

    private fun showPrepStageButtons() {
        binding.pauseButton.animate().translationX(0f)
        binding.startButton.animate().translationX(0f)
        binding.startButton.text = resources.getText(R.string.start_button)
        binding.pauseButton.visibility = View.INVISIBLE
        binding.startButton.visibility = View.VISIBLE
        binding.restartButton.visibility = View.INVISIBLE
    }

    private fun showPauseStageButtons() {
        binding.startButton.text = resources.getText(R.string.resume_button)
        binding.pauseButton.visibility = View.INVISIBLE
        binding.startButton.visibility = View.VISIBLE
    }

    private fun showRestartStageButtons() {
        binding.restartButton.animate().translationX(0f)
        binding.pauseButton.visibility = View.INVISIBLE
        binding.startButton.visibility = View.INVISIBLE
    }


}


