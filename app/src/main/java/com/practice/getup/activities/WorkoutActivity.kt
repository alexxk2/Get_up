package com.practice.getup.activities

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.practice.getup.R
import com.practice.getup.databinding.ActivityWorkoutBinding
import com.practice.getup.model.Options
import com.practice.getup.model.SuperTimer
import com.practice.getup.model.SuperTimer2

class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutBinding
    private lateinit var options: Options
    private lateinit var timer: CountDownTimer
    private lateinit var testTimer: SuperTimer
    private lateinit var testTimer2: SuperTimer2
    private lateinit var countDownPlayer: MediaPlayer
    private lateinit var workTimePlayer: MediaPlayer
    private lateinit var restTimePlayer: MediaPlayer
    private lateinit var workoutFinishPlayer: MediaPlayer

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

        //рабочий таймер закомментирован
        /*countDownPlayer = MediaPlayer.create(this, R.raw.sound_countdown)
        workTimePlayer = MediaPlayer.create(this, R.raw.sound_work_start)
        restTimePlayer = MediaPlayer.create(this, R.raw.sound_rest_start)
        workoutFinishPlayer = MediaPlayer.create(this, R.raw.sound_workout_finish)

        val preparationTime = (options.preparingTime * 1000).toLong()

        val totalWorkoutTime =
            with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()

        totalTimeForLocalTimer = preparationTime
        totalTimeForGlobalTimer = totalWorkoutTime

        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime, preparationTime)
*/

        /*binding.startButton.setOnClickListener {
            startTimer()

        }
        binding.pauseButton.setOnClickListener { pauseTimer() }

        binding.restartButton.setOnClickListener { restartTimer() }*/
        createTimer2()

        binding.startButton.setOnClickListener {
            testTimer2.start()

        }
        binding.pauseButton.setOnClickListener { testTimer2.pauseTimer() }

        binding.restartButton.setOnClickListener { testTimer2.restartTimer() }

    }

    /*private fun startTimer() {

        if (isTimerOn) return
        switchStagesNames()

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

                    *//*  workoutFinishPlayer.start()*//*
                    updateGlobalProgressIndicator(
                        fixedTimeForSet,
                        0
                    )

                    return
                }
                startTimer()
            }
        }.start()
        isTimerOn = true
        switchControlButton()
    }


    private fun pauseTimer() {
        if (!isTimerOn) return
        timer.cancel()
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
        binding.generalTimerView.text = calculateTimeForTimersUpdaters(totalSecondsLeft)
    }

    private fun updateLocalTime(millisUntilFinished: Long) {
        val totalSecondsLeft = (millisUntilFinished / 1000).toInt()
        binding.localTimerView.text = calculateTimeForTimersUpdaters(totalSecondsLeft)
    }

    private fun calculateTimeForTimersUpdaters(totalSecondsLeft: Int): String {

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
        fixedTimeForSet: Long,
        millisUntilFinished: Long
    ) {
        val totalTimeMs = with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
        val totalTimeSec = (totalTimeMs / 1000).toDouble()
        val timePassedSec =
            (((fixedTimeForSet + timePassed) - millisUntilFinished) / 1000).toDouble()
        binding.globalProgressIndicator.progress = (timePassedSec / totalTimeSec * 100).toInt()
    }


    //надо отрефакторить
    private fun switchStagesNames() {

        val workSetsLeft = "${(options.numberOfSets - setsDone / 2)} left"
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

    }*/

    companion object {
        const val OPTIONS = "OPTIONS"
    }


    //делаю инкапсулированный объект #1
    private fun createTimer() {


        testTimer = object : SuperTimer((options.preparingTime * 1000).toLong(), 1000) {


            val countDownPlayer: MediaPlayer = MediaPlayer()
            val workTimePlayer: MediaPlayer = MediaPlayer()
            val restTimePlayer: MediaPlayer = MediaPlayer()
            val workoutFinishPlayer: MediaPlayer = MediaPlayer()

            var totalTimeForLocalTimer: Long = (options.preparingTime * 1000).toLong()
            var totalTimeForGlobalTimer: Long =
                with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
            var isTimerOn = false

            var setsDone = -1
            var isWorkTime: Boolean? = null
            var timePassed: Long = 0

            val workTime = (options.workTime * 1000).toLong()
            val restTime = (options.restTime * 1000).toLong()
            val preparationTime = (options.preparingTime * 1000).toLong()
            val numberOfSets = (options.numberOfSets)
            val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime

            val fixedSetTime = when (isWorkTime) {
                null -> preparationTime
                true -> workTime
                false -> restTime
            }

            init {
                updateLocalTime(preparationTime)
                updateGlobalTime(preparationTime)
            }


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

                timePassed += when (isWorkTime) {
                    null -> preparationTime
                    true -> workTime
                    false -> restTime
                }
                //10 - 10
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
                    null -> workoutFinishPlayer.start()
                }

                if (setsDone == options.numberOfSets * 2) {
                    binding.startButton.visibility = View.INVISIBLE
                    binding.pauseButton.visibility = View.INVISIBLE
                    binding.restartButton.visibility = View.VISIBLE

                    /*  workoutFinishPlayer.start()*/
                    updateGlobalProgressIndicator(
                        0
                    )

                    return
                }
                testTimer.start()
            }

            override fun pauseTimer() {
                if (!isTimerOn) return
                timer.cancel()
                isTimerOn = false
                switchControlButton()

            }

            override fun restartTimer() {
                if (isTimerOn) return
                //simply sets all values to default, может как то упростить установку на дефолт
                totalTimeForLocalTimer = ((options.preparingTime) * 1000).toLong()
                totalTimeForGlobalTimer =
                    with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
                setsDone = -1
                isWorkTime = null
                timePassed = 0
                testTimer.start()
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


            private fun updateGlobalProgressIndicator(millisUntilFinished: Long) {
                val totalTimeMs =
                    with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
                val totalTimeSec = (totalTimeMs / 1000).toDouble()
                val timePassedSec =
                    (((fixedSetTime + timePassed) - millisUntilFinished) / 1000).toDouble()
                binding.globalProgressIndicator.progress =
                    (timePassedSec / totalTimeSec * 100).toInt()
            }


            //надо отрефакторить
            private fun switchStagesNames() {

                val workSetsLeft = "${(options.numberOfSets - setsDone / 2)} left"
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


    }

    //делаю инкапсулированный объект #2
    private fun createTimer2() {

        testTimer2 = object : SuperTimer2(workoutOptions = options) {


            private lateinit var temporaryTimer: CountDownTimer

            val workTime = (options.workTime * 1000).toLong()
            val restTime = (options.restTime * 1000).toLong()
            val preparationTime = (options.preparingTime * 1000).toLong()
            val numberOfSets = (options.numberOfSets)
            val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime

            val countDownPlayer: MediaPlayer = MediaPlayer()
            val workTimePlayer: MediaPlayer = MediaPlayer()
            val restTimePlayer: MediaPlayer = MediaPlayer()
            val workoutFinishPlayer: MediaPlayer = MediaPlayer()


            var totalTimeForLocalTimer: Long = (options.preparingTime * 1000).toLong()
            var totalTimeForGlobalTimer: Long =
                with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
            var isTimerOn = false

            var setsDone = -1
            var isWorkTime: Boolean? = null
            var timePassed: Long = 0
            var fixedSetTime = (options.preparingTime * 1000).toLong()

            init {
                updateLocalTime(preparationTime)
                updateGlobalTime(preparationTime)

            }

            //старт таймера
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

                        timePassed += when (isWorkTime) {
                            null -> preparationTime
                            true -> workTime
                            false -> restTime
                        }

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
                            null -> workoutFinishPlayer.start()
                        }

                        if (setsDone == options.numberOfSets * 2) {
                            binding.startButton.visibility = View.INVISIBLE
                            binding.pauseButton.visibility = View.INVISIBLE
                            binding.restartButton.visibility = View.VISIBLE
                            //косячок, не доводит прогресс бар одновременно с последней секундой
                            //а только после неё, даже костыль не работает надо метод смотреть
                            updateGlobalProgressIndicator(0)

                            return
                        }
                        testTimer2.start()
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
                totalTimeForLocalTimer = ((options.preparingTime) * 1000).toLong()
                totalTimeForGlobalTimer =
                    with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
                setsDone = -1
                isWorkTime = null
                timePassed = 0
                testTimer2.start()
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


            private fun updateGlobalProgressIndicator(millisUntilFinished: Long) {
                val totalTimeMs =
                    with(options) { ((workTime + restTime) * numberOfSets + preparingTime) * 1000 }.toLong()
                val totalTimeSec = (totalTimeMs / 1000).toDouble()
                val timePassedSec =
                    (((fixedSetTime + timePassed) - millisUntilFinished) / 1000).toDouble()
                binding.globalProgressIndicator.progress =
                    (timePassedSec / totalTimeSec * 100).toInt()
            }


            //надо отрефакторить
            private fun switchStagesNames() {

                val workSetsLeft = "${(options.numberOfSets - setsDone / 2)} left"
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


    }

}


