package com.practice.getup.presentation.timer.ui


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practice.getup.R
import com.practice.getup.domain.models.Workout
import com.practice.getup.presentation.timer.adapter.WorkoutAdapter
import com.practice.getup.databinding.FragmentWorkoutBinding
import com.practice.getup.presentation.timer.models.SoundStages
import com.practice.getup.presentation.timer.models.TimerStages
import com.practice.getup.presentation.timer.view_model.TimerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class TimerFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TimerViewModel by viewModel { parametersOf(workout)}
    private lateinit var adapter: WorkoutAdapter
    private lateinit var workout: Workout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            workout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(WORKOUT, Workout::class.java)!!
            } else it.getParcelable(WORKOUT)!!
        }

        viewModel.prepareTimer()
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
            Runnable { viewModel.workoutStagePosition.value?.let { scrollToCurrentStage(it) } }
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

        viewModel.workoutStagePosition.observe(viewLifecycleOwner) { currentStagePosition ->
            scrollToCurrentStage(currentStagePosition)
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

    private fun showFinishConfirmationDialog() {
        val action = TimerFragmentDirections.actionTimerFragmentToMainFragment()

        if (viewModel.timerStage.value == TimerStages.PREPARATION) {
            viewModel.restartTimer()
            findNavController().navigate(action)
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.alert_dialog_title))
                .setMessage(getString(R.string.alert_dialog_message_finish))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.answer_no)) { _, _ -> }
                .setPositiveButton(getString(R.string.answer_yes)) { _, _ ->
                    viewModel.restartTimer()
                    findNavController().navigate(action)
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val WORKOUT = "workout"
    }
}