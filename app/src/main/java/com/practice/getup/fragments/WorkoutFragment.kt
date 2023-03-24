package com.practice.getup.fragments


import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.getup.R
import com.practice.getup.activities.ViewModelFactoryFragments
import com.practice.getup.adapters.WorkoutAdapter
import com.practice.getup.databinding.FragmentWorkoutBinding
import com.practice.getup.model.Options
import com.practice.getup.model.SoundStages
import com.practice.getup.model.TimerStages
import com.practice.getup.viewModels.WorkoutViewModel
import kotlinx.coroutines.Runnable


class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var options: Options
    private val viewModel: WorkoutViewModel by viewModels{ViewModelFactoryFragments(options) }
    private lateinit var adapter: WorkoutAdapter
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
                options = it.getParcelable(OPTIONS)?: Options.DEFAULT
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WorkoutAdapter(requireContext())
        adapter.dataSet = viewModel.stageList.value!!
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        binding.recyclerView.post(
            Runnable { viewModel.currentStagePosition.value?.let { scrollToCurrentStage(it) } }
        )

        viewModel.timerStage.observe(viewLifecycleOwner) { timerStage ->
            switchControlButton(timerStage)
        }

        viewModel.localTimeToShow.observe(viewLifecycleOwner) { localTimeToShow ->
            binding.localTimerView.text = localTimeToShow
        }

        viewModel.globalTimeToShow.observe(viewLifecycleOwner) { globalTimeToShow ->
            binding.generalTimerView.text = globalTimeToShow
        }

        viewModel.indicatorProgressValue.observe(viewLifecycleOwner) { indicatorProgressValue ->
            binding.globalProgressIndicator.progress = indicatorProgressValue
        }

        viewModel.stageList.observe(viewLifecycleOwner) { stageList ->
            adapter.dataSet = stageList
        }

        viewModel.currentStagePosition.observe(viewLifecycleOwner) { currentStagePosition ->
            scrollToCurrentStage(currentStagePosition)
        }

        viewModel.soundStages.observe(viewLifecycleOwner) { soundStage ->
            playTimerSound(soundStage)
        }

        binding.startButton.setOnClickListener {
            viewModel.startTimer()

        }
        binding.pauseButton.setOnClickListener { viewModel.pauseTimer() }
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
        binding.recyclerView.smoothScrollToPosition(currentStagePosition)
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
            mediaPlayer = MediaPlayer.create(context, soundRes)
            mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val OPTIONS = "options"
    }
}