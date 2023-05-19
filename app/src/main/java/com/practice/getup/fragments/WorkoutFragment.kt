package com.practice.getup.fragments


import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practice.getup.App
import com.practice.getup.R
import com.practice.getup.activities.ViewModelFactoryFragments
import com.practice.getup.adapters.WorkoutAdapter
import com.practice.getup.database.Workout
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
    private val viewModel: WorkoutViewModel by viewModels {
        ViewModelFactoryFragments(workout)
    }
    private lateinit var adapter: WorkoutAdapter
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var workout: Workout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            options = it.getParcelable(OPTIONS) ?: Options.DEFAULT

            workout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(WORKOUT, Workout::class.java)!!
            } else it.getParcelable(WORKOUT)!!
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            showFinishConfirmationDialog()
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
        binding.backButton.setOnClickListener {
            showFinishConfirmationDialog()
        }

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

    private fun showFinishConfirmationDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_message_finish))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.answer_no)){_,_ ->}
            .setPositiveButton(getString(R.string.answer_yes)){_,_ ->
                val action = WorkoutFragmentDirections.actionWorkoutFragmentToMainFragment()
                findNavController().navigate(action)
            }
            .show()
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
        const val WORKOUT = "workout"
    }
}