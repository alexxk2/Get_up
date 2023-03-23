package com.practice.getup.activities

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.getup.R
import com.practice.getup.viewModels.WorkoutViewModel
import com.practice.getup.adapters.WorkoutAdapter
import com.practice.getup.databinding.ActivityWorkoutBinding
import com.practice.getup.model.SoundStages
import com.practice.getup.model.TimerStages


class WorkoutActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var adapter: WorkoutAdapter

    private var mediaPlayer: MediaPlayer? = null
    private val viewModel: WorkoutViewModel by viewModels { ViewModelFactory(this) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutBinding.inflate(layoutInflater).also { setContentView(it.root) }

        adapter = WorkoutAdapter(this)
        adapter.dataSet = viewModel.stageList.value!!
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

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
            scrollToCurrentStage(currentStagePosition)
        }

        viewModel.soundStages.observe(this) { soundStage ->
            playTimerSound(soundStage)
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
        if (currentStagePosition >= 0) binding.recyclerView.smoothScrollToPosition(currentStagePosition)
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

    private fun playTimerSound(soundStage: SoundStages) {

        mediaPlayer?.release()
        mediaPlayer = null

        when (soundStage) {
            SoundStages.COUNTDOWN -> {
                createMediaPlayer(R.raw.sound_countdown)
            }
            SoundStages.WORK -> {
                createMediaPlayer(R.raw.sound_work_start)
            }
            SoundStages.REST -> {
                createMediaPlayer(R.raw.sound_rest_start)
            }
            SoundStages.FINISH -> {
                createMediaPlayer(R.raw.sound_workout_finish)
            }
        }
    }

    private fun createMediaPlayer(soundRes: Int){
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(application, soundRes)
            mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}


