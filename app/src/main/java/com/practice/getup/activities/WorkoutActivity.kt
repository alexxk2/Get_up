package com.practice.getup.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.Animation.RESTART
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import com.practice.getup.R
import com.practice.getup.ViewModels.WorkoutViewModel
import com.practice.getup.databinding.ActivityWorkoutBinding
import com.practice.getup.model.TimerStages


class WorkoutActivity : AppCompatActivity() {


    private lateinit var binding: ActivityWorkoutBinding



    private val viewModel: WorkoutViewModel by viewModels{ViewModelFactory(this)}
    //private lateinit var timer: SuperTimer


    //TODO не сохраняет текущее положение данных при повороте во всех активностях
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityWorkoutBinding.inflate(layoutInflater).also { setContentView(it.root) }

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

        binding.startButton.setOnClickListener { viewModel.startTimer() }
        binding.pauseButton.setOnClickListener { viewModel.pauseTimer() }
        //TODO изменить цвет нажатой клавиши - сейчас фиолетовый
        binding.restartButton.setOnClickListener { viewModel.restartTimer() }

       /* createTimer(this)

        binding.startButton.setOnClickListener {
            timer.start()

        }
        binding.pauseButton.setOnClickListener { timer.pauseTimer() }

        binding.restartButton.setOnClickListener { timer.restartTimer() }*/


    }

    private fun switchControlButton(timerStage: TimerStages) {
        when (timerStage) {

            TimerStages.PREPARATION -> {
               showPrepStageButtons()
            }

            TimerStages.RESUME -> {
                showResumeStageButtons()
                //binding.pauseButton.animation = animationStartToLeft
               // binding.restartButton.animation = animationStartToRight
            }
            TimerStages.PAUSE -> {
                showPauseStageButtons()
            }
            TimerStages.RESTART -> {
                showRestartStageButtons()
                //binding.startButton.animation = animationEndToRight
               // binding.pauseButton.animation = animationEndToRight
                //binding.restartButton.animation = animationEndToLeft
            }
        }
    }

//    TODO идея: сделать recycler view (кастомный с анимацией) и поместить его в объект таймера, там можно
//    будет привязать вьюхолдеры к любым данным в объекте, а значит можно будет вертеть recycler view
//    как хочешь и кнопки сделать активные, надо найти как сделать, чтобы recycler view сам крутился +
//    была одна выделенная вьюшка
   /* private fun createTimer(context: Context) {


        timer = object : SuperTimer() {

            private lateinit var temporaryTimer: CountDownTimer

            val workTime = (_options.workTime * 1000).toLong()
            val restTime = (_options.restTime * 1000).toLong()
            val preparationTime = (_options.preparingTime * 1000).toLong()
            val numberOfSets = (_options.numberOfSets)
            val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime

            val countDownPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound_countdown)
            val workTimePlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound_work_start)
            val restTimePlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound_rest_start)
            val finishTimePlayer: MediaPlayer =
                MediaPlayer.create(context, R.raw.sound_workout_finish)


            var totalTimeForLocalTimer: Long = preparationTime
            var totalTimeForGlobalTimer: Long = totalWorkoutTime
            var fixedSetTime = preparationTime
            var isTimerOn = false
            var setsDone = -1
            var isWorkTime: Boolean? = null
            var timePassed: Long = 0


            init {
                updateLocalTime(preparationTime)
                updateGlobalTime(preparationTime)

            }

            override fun start() {

                if (isTimerOn) return

                switchStagesNames()

                fixedSetTime = when (isWorkTime) {
                    null -> preparationTime
                    true -> workTime
                    false -> restTime
                }

                temporaryTimer = object : CountDownTimer(totalTimeForLocalTimer, 1000) {

                    override fun onTick(millisUntilFinished: Long) {
                        isTimerOn = true
                        switchStagesNames()
                        //allows to resume to the timer with the same time left
                        totalTimeForLocalTimer = millisUntilFinished

                        updateLocalTime(millisUntilFinished)
                        updateGlobalTime(millisUntilFinished)
                        updateGlobalProgressIndicator(
                            millisUntilFinished
                        )
                        if (millisUntilFinished <= 3000) countDownPlayer.start()

                    }

                    override fun onFinish() {
                        setsDone++
                        isTimerOn = false
                        timePassed += fixedSetTime

                        totalTimeForGlobalTimer -= fixedSetTime

                        if (isWorkTime == false || isWorkTime == null) {
                            totalTimeForLocalTimer = workTime
                            isWorkTime = true

                        } else {
                            totalTimeForLocalTimer = restTime
                            isWorkTime = false
                        }


                        if (setsDone == _options.numberOfSets * 2) isWorkTime = null

                        when (isWorkTime) {
                            true -> workTimePlayer.start()
                            false -> restTimePlayer.start()
                            null -> finishTimePlayer.start()
                        }

                        if (setsDone == _options.numberOfSets * 2) {
                            binding.startButton.visibility = View.INVISIBLE
                            binding.pauseButton.visibility = View.INVISIBLE
                            binding.restartButton.visibility = View.VISIBLE

                            return
                        }
                        timer.start()
                    }

                }.start()
                isTimerOn = true
                switchControlButton()
            }


            override fun pauseTimer() {
                if (!isTimerOn) return
                temporaryTimer.cancel()
                isTimerOn = false
                switchControlButton()

            }

            override fun restartTimer() {
                if (isTimerOn) return
                //simply sets all values to default, может как то упростить установку на дефолт
                totalTimeForLocalTimer = ((_options.preparingTime) * 1000).toLong()
                totalTimeForGlobalTimer =
                    with(_options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
                setsDone = -1
                isWorkTime = null
                timePassed = 0
                binding.restartButton.visibility = View.INVISIBLE
                timer.start()
            }

            private fun switchControlButton() {
                if (isTimerOn) {
                    binding.startButton.visibility = View.INVISIBLE
                    binding.pauseButton.visibility = View.VISIBLE
                } else {
                    binding.pauseButton.visibility = View.INVISIBLE
                    binding.startButton.visibility = View.VISIBLE
                    binding.startButton.text = resources.getText(R.string.resume_button)
                }
            }

            private fun updateGlobalTime(millisUntilFinished: Long) {

                val timePassed = fixedSetTime - millisUntilFinished
                val totalSecondsLeft = ((totalTimeForGlobalTimer - timePassed) / 1000).toInt()//9000
                binding.generalTimerView.text = calculateTimeForTimersUpdaters(totalSecondsLeft)
            }

            private fun updateLocalTime(millisUntilFinished: Long) {
                val totalSecondsLeft = (millisUntilFinished / 1000).toInt()
                binding.localTimerView.text = calculateTimeForTimersUpdaters(totalSecondsLeft)
            }

            private fun calculateTimeForTimersUpdaters(totalSecondsLeft: Int): String {

                val secondsToShow = (totalSecondsLeft % 60).toString().padStart(2, '0')
                val minutesToShow = ((totalSecondsLeft / 60) % 60).toString().padStart(2, '0')
                val hoursToShow = (totalSecondsLeft / 3600).toString().padStart(2, '0')

                val timeToShow = when {
                    hoursToShow.toInt() > 0 -> "$hoursToShow : $minutesToShow : $secondsToShow"
                    else -> "$minutesToShow : $secondsToShow"
                }
                return timeToShow
            }


            private fun updateGlobalProgressIndicator(millisUntilFinished: Long) {

                val totalTimeSec = (totalWorkoutTime / 1000).toDouble()
                val onTickMethodCorrection: Long = 1000
                val timePassedSec =
                    (((timePassed + fixedSetTime) - (millisUntilFinished - onTickMethodCorrection)) / 1000).toDouble()

                binding.globalProgressIndicator.progress =
                    (timePassedSec / totalTimeSec * 100).toInt()
            }


            //надо отрефакторить
            private fun switchStagesNames() {

                val workSetsLeft = "${(_options.numberOfSets - setsDone / 2)} left"
                with(binding) {
                    when (isWorkTime) {
                        null -> {
                            upcomingStageView.text = getString(R.string.work_text)
                            currentStageView.text = getString(R.string.preparation_text)
                            complitedStageView.visibility = View.INVISIBLE
                            setsLeftView.visibility = View.INVISIBLE
                        }
                        true -> {
                            upcomingStageView.text = getString(R.string.rest_text)
                            currentStageView.text = getString(R.string.work_text)
                            complitedStageView.visibility = View.VISIBLE
                            if (setsDone == 0) {
                                complitedStageView.text = getString(R.string.preparation_text)
                            } else complitedStageView.text = getString(R.string.rest_text)
                            setsLeftView.visibility = View.VISIBLE
                            setsLeftView.text = workSetsLeft
                        }
                        false -> {
                            upcomingStageView.text = getString(R.string.work_text)
                            currentStageView.text = getString(R.string.rest_text)
                            complitedStageView.text = getString(R.string.work_text)
                            setsLeftView.visibility = View.INVISIBLE
                        }
                    }
                }

            }

        }


    }*/

    companion object{
        const val OPTIONS = "OPTIONS"
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


