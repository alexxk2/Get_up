package com.practice.getup.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.practice.getup.R
import com.practice.getup.databinding.ActivityWorkoutBinding
import com.practice.getup.model.Options

class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var options: Options
    private lateinit var timer: CountDownTimer

    private var totalTimeForLocalTimer: Long = 0
    private var totalTimeForGlobalTimer: Long = 0
    private var isTimerOn = false

    private var setsDone = -1
    private var isWorkTime: Boolean? = null
    private var timePassed: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        options = intent.getParcelableExtra(OPTIONS)!!
        binding = ActivityWorkoutBinding.inflate(layoutInflater).also { setContentView(it.root) }


        val preparationTime = (options.preparingTime * 1000).toLong()

        val totalWorkoutTime =
            with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()

        totalTimeForLocalTimer = preparationTime
        totalTimeForGlobalTimer = totalWorkoutTime

        binding.startButton.setOnClickListener {
            startTimer()
        }
        binding.pauseButton.setOnClickListener { pauseTimer() }

        binding.restartButton.setOnClickListener { restartTimer() }


    }

    private fun startTimer() {

        if (isTimerOn) return

        timer = object : CountDownTimer(totalTimeForLocalTimer, 1000) {

            val workTime = (options.workTime * 1000).toLong()
            val restTime = (options.restTime * 1000).toLong()
            val preparationTime = (options.preparingTime * 1000).toLong()
            val numberOfSets = (options.numberOfSets)
            val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime

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
                binding.currentStageView.text = setsDone.toString()

                if (isWorkTime == true || isWorkTime == null) {
                    totalTimeForLocalTimer = restTime
                    isWorkTime = false

                } else {
                    totalTimeForLocalTimer = workTime
                    isWorkTime = true
                }

                if (setsDone == options.numberOfSets * 2) {
                    binding.currentStageView.text = "Тренировка закончена"
                    binding.startButton.visibility = View.INVISIBLE
                    binding.pauseButton.visibility = View.INVISIBLE
                    binding.restartButton.visibility = View.VISIBLE
                    return
                }
                startTimer()

            }
        }.start()
        isTimerOn = true
        switchControlButton()
    }

    //потом подумать над текстовыми полями над таймером
    private fun pauseTimer() {
        if (!isTimerOn) return
        timer.cancel()
        isTimerOn = false
        switchControlButton()

    }

    //перенести все в отдельный класс таймер после добавления текстовых полей и тд
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

    private fun updateGlobalTime(fixedTimeForSet: Long, millisUntilFinished: Long) {
        val timePassed = fixedTimeForSet - millisUntilFinished
        val totalSecondsLeft = ((totalTimeForGlobalTimer - timePassed) / 1000).toInt()
        binding.generalTimerView.text = calculateTimeToShow(totalSecondsLeft)
    }

    private fun updateLocalTime(millisUntilFinished: Long) {
        val totalSecondsLeft = (millisUntilFinished / 1000).toInt()
        binding.localTimerView.text = calculateTimeToShow(totalSecondsLeft)
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

    private fun updateGlobalProgressIndicator(
        totalTimeMs: Long,
        fixedTimeForSet: Long,
        millisUntilFinished: Long
    ) {
        val totalTimeSec = (totalTimeMs / 1000).toDouble()
        val timePassedSec = (((fixedTimeForSet + timePassed) - millisUntilFinished) / 1000).toDouble()
        binding.globalProgressIndicator.progress = (timePassedSec / totalTimeSec * 100).toInt()
    }

    private fun switchStagesNames(){
        with(binding){
            when (isWorkTime) {
                null -> {
                   /*currentStageView.text = resources.*/
                }
                true -> {

                }
                false -> {

                }
            }
        }

    }


    companion object {
        const val OPTIONS = "OPTIONS"
    }
}