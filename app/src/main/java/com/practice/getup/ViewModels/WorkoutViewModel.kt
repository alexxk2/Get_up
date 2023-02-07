package com.practice.getup.ViewModels

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.ViewModel
import com.practice.getup.R
import com.practice.getup.model.Options

class WorkoutViewModel(private val options: Options) : ViewModel() {


    private lateinit var timer: CountDownTimer

    private val workTime = (options.workTime * 1000).toLong()
    private val restTime = (options.restTime * 1000).toLong()
    private val preparationTime = (options.preparingTime * 1000).toLong()
    private val numberOfSets = (options.numberOfSets)
    private val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime

    //переместить создание в паблик функцию
    val countDownPlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_countdown)
    val workTimePlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_work_start)
    val restTimePlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_rest_start)
    val finishTimePlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_workout_finish)

    private var totalTimeForLocalTimer: Long = preparationTime
    private var totalTimeForGlobalTimer: Long = totalWorkoutTime
    private var fixedSetTime = preparationTime

    private var _isTimerOn = false
    val isTimerOn = _isTimerOn

    private var setsDone = -1
    private var isWorkTime: Boolean? = null
    private var timePassed: Long = 0


    private fun start() {

        if (_isTimerOn) return

        switchStagesNames()

        fixedSetTime = when (isWorkTime) {
            null -> preparationTime
            true -> workTime
            false -> restTime
        }

        timer = object : CountDownTimer(totalTimeForLocalTimer, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                _isTimerOn = true
                switchStagesNames()
                //allows to resume to the timer with the same time left
                totalTimeForLocalTimer = millisUntilFinished

                //updateLocalTime(millisUntilFinished)
                //updateGlobalTime(millisUntilFinished)
                /*updateGlobalProgressIndicator(
                    millisUntilFinished
                )*/
                if (millisUntilFinished <= 3000) countDownPlayer.start()

            }

            override fun onFinish() {
                setsDone++
                _isTimerOn = false
                timePassed += fixedSetTime

                totalTimeForGlobalTimer -= fixedSetTime

                if (isWorkTime == false || isWorkTime == null) {
                    totalTimeForLocalTimer = workTime
                    isWorkTime = true

                } else {
                    totalTimeForLocalTimer = restTime
                    isWorkTime = false
                }


                if (setsDone == options.numberOfSets * 2) isWorkTime = null

                when (isWorkTime) {
                    true -> workTimePlayer.start()
                    false -> restTimePlayer.start()
                    null -> finishTimePlayer.start()
                }

                if (setsDone == options.numberOfSets * 2) {

                    return
                }
                timer.start()
            }

        }.start()
        _isTimerOn = true
        switchControlButton()
    }



    private fun pauseTimer() {
        if (!_isTimerOn) return
        timer.cancel()
        _isTimerOn = false
        switchControlButton()

    }

    private fun restartTimer() {
        if (_isTimerOn) return
        //simply sets all values to default, может как то упростить установку на дефолт
        totalTimeForLocalTimer = ((options.preparingTime) * 1000).toLong()
        totalTimeForGlobalTimer =
            with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
        setsDone = -1
        isWorkTime = null
        timePassed = 0
        binding.restartButton.visibility = View.INVISIBLE
        timer.start()
    }

    //TODO готово для переноса
    private fun switchControlButton(isTimerOn:Boolean) {
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

        val workSetsLeft = "${(options.numberOfSets - setsDone / 2)} left"
        with(binding) {
            when (isWorkTime) {
                null -> {
                    upcomingStageView.text = getString(com.practice.getup.R.string.work_text)
                    currentStageView.text = getString(com.practice.getup.R.string.preparation_text)
                    complitedStageView.visibility = android.view.View.INVISIBLE
                    setsLeftView.visibility = android.view.View.INVISIBLE
                }
                true -> {
                    upcomingStageView.text = getString(com.practice.getup.R.string.rest_text)
                    currentStageView.text = getString(com.practice.getup.R.string.work_text)
                    complitedStageView.visibility = android.view.View.VISIBLE
                    if (setsDone == 0) {
                        complitedStageView.text =
                            getString(com.practice.getup.R.string.preparation_text)
                    } else complitedStageView.text =
                        getString(com.practice.getup.R.string.rest_text)
                    setsLeftView.visibility = android.view.View.VISIBLE
                    setsLeftView.text = workSetsLeft
                }
                false -> {
                    upcomingStageView.text = getString(com.practice.getup.R.string.work_text)
                    currentStageView.text = getString(com.practice.getup.R.string.rest_text)
                    complitedStageView.text = getString(com.practice.getup.R.string.work_text)
                    setsLeftView.visibility = android.view.View.INVISIBLE
                }
            }
        }

    }
}