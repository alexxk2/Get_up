/*
package com.practice.getup.model

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.View
import com.practice.getup.R


class Timer(private val context: Context, private val options: Options) :
    CountDownTimer((options.preparingTime * 1000).toLong(), 1000) {


    private var totalTimeForLocalTimer: Long = (options.preparingTime * 1000).toLong()
    private var totalTimeForGlobalTimer: Long =
        with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()


    private var isTimerOn = false
    private var setsDone = -1 //для передачи во вьюшку

    private var isWorkTime: Boolean? = null //для передачи во вьюшку
    private var timePassed: Long = 0

    // плееры для звуков
    private val countDownPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound_countdown)
    private val workTimePlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound_work_start)
    private val restTimePlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound_rest_start)
    private val workoutFinishPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound_workout_finish)

    //константы из options
    private val preparationTime = (options.preparingTime * 1000).toLong()
    private val workTime = (options.workTime * 1000).toLong()
    private val restTime = (options.restTime * 1000).toLong()
    private val numberOfSets = (options.numberOfSets)
    private val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime


    //параметры для передачи во вьюшки
    private lateinit var localTime: String
    private lateinit var globalTime: String
    private var progressNumberForIdicator: Int = 0


    val fixedTimeForSet = when (isWorkTime) {
        null -> preparationTime
        true -> workTime
        false -> restTime
    }

    override fun onTick(millisUntilFinished: Long) {

        //allows to resume to the timer with the same time left
        totalTimeForLocalTimer = millisUntilFinished

        updateLocalTime(millisUntilFinished)
        updateGlobalTime(fixedTimeForSet, millisUntilFinished)
        updateGlobalProgressIndicator(
            totalWorkoutTime,
            fixedTimeForSet,
            millisUntilFinished
        )
        if (millisUntilFinished <= 3000) countDownPlayer.start()
    }

    override fun onFinish() {
        setsDone++
        isTimerOn = false

        timePassed += when (isWorkTime) {
            null -> preparationTime
            true -> workTime
            false -> restTime
        }

        totalTimeForGlobalTimer -= fixedTimeForSet

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
            null -> workoutFinishPlayer.start()
        }

        if (setsDone == options.numberOfSets * 2) {
            binding.startButton.visibility = View.INVISIBLE
            binding.pauseButton.visibility = View.INVISIBLE
            binding.restartButton.visibility = View.VISIBLE

  workoutFinishPlayer.start()

            updateGlobalProgressIndicator(
                totalWorkoutTime,
                fixedTimeForSet,
                0
            )

            return
        }
        start()
        isTimerOn = true
        switchControlButton()
    }


    private fun pauseTimer() {
        if (!isTimerOn) return
        cancel()
        isTimerOn = false
        switchControlButton()

    }

    private fun restartTimer() {
        if (isTimerOn) return
        //simply sets all values to default, может как то упростить установку на дефолт
        totalTimeForLocalTimer = ((options.preparingTime) * 1000).toLong()
        totalTimeForGlobalTimer =
            with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
        setsDone = -1
        isWorkTime = null
        timePassed = 0
        startTimer()
        binding.restartButton.visibility = View.INVISIBLE
    }



    private fun updateGlobalTime(fixedTimeForSet: Long, millisUntilFinished: Long) {
        val timePassed = fixedTimeForSet - millisUntilFinished
        val totalSecondsLeft = ((totalTimeForGlobalTimer - timePassed) / 1000).toInt()
        globalTime = calculateTimeToShow(totalSecondsLeft)
    }

    private fun updateLocalTime(millisUntilFinished: Long) {
        val totalSecondsLeft = (millisUntilFinished / 1000).toInt()
        localTime = calculateTimeToShow(totalSecondsLeft)
    }

    //padStart adds 0 before time figure if necessary
    private fun calculateTimeToShow(totalSecondsLeft: Int): String {

        val secondsToShow = (totalSecondsLeft % 60).toString().padStart(2, '0')
        val minutesToShow = ((totalSecondsLeft / 60) % 60).toString().padStart(2, '0')
        val hoursToShow = (totalSecondsLeft / 3600)

        val timeToShow = when {
            (hoursToShow > 0) -> {
                hoursToShow.toString().padStart(2, '0')
                "$hoursToShow : $minutesToShow : $secondsToShow"
            }
            else -> "$minutesToShow : $secondsToShow"
        }
        return timeToShow
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

    private fun updateGlobalProgressIndicator(
        totalTimeMs: Long,
        fixedTimeForSet: Long,
        millisUntilFinished: Long
    ) {
        val totalTimeSec = (totalTimeMs / 1000).toDouble()
        val timePassedSec =
            (((fixedTimeForSet + timePassed) - millisUntilFinished) / 1000).toDouble()
        progressNumberForIdicator = (timePassedSec / totalTimeSec * 100).toInt()
    }



}
*/
