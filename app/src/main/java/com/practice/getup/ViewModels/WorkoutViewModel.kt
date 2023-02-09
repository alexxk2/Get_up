package com.practice.getup.ViewModels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.getup.model.Options
import com.practice.getup.model.TimerStages

class WorkoutViewModel(private val options: Options) : ViewModel() {


    private lateinit var timer: CountDownTimer

    private val workTime = (options.workTime * 1000).toLong()
    private val restTime = (options.restTime * 1000).toLong()
    private val preparationTime = (options.preparingTime * 1000).toLong()
    private val numberOfSets = (options.numberOfSets)
    private val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime


    //переместить создание в активити
    /*val countDownPlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_countdown)
    val workTimePlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_work_start)
    val restTimePlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_rest_start)
    val finishTimePlayer: MediaPlayer = MediaPlayer.create(getApplication(), R.raw.sound_workout_finish)*/

    private var totalTimeForLocalTimer: Long = preparationTime
    private var totalTimeForGlobalTimer: Long = totalWorkoutTime
    private var fixedSetTime = preparationTime


    private var setsDone = -1
    private var isWorkTime: Boolean? = null
    private var timePassed: Long = 0

    //тестовая переменная
    var test = 0L

    private var _timerStage = MutableLiveData(TimerStages.PREPARATION)
    val timerStage: LiveData<TimerStages> = _timerStage

    private val _isTimerOn = MutableLiveData<Boolean?>(null)
    val isTimerOn: LiveData<Boolean?> = _isTimerOn

    private val _localTimeToShow = MutableLiveData("")
    val localTimeToShow: LiveData<String> = _localTimeToShow

    private val _globalTimeToShow = MutableLiveData("")
    val globalTimeToShow: LiveData<String> = _globalTimeToShow

    private val _indicatorProgressValue = MutableLiveData<Int>()
    val indicatorProgressValue: LiveData<Int> = _indicatorProgressValue


    init {
        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime)
        _indicatorProgressValue.value = 0
    }


     fun startTimer() {

        if (_isTimerOn.value == true) return
        //TODO перенести свичстейджес
        //switchStagesNames()

        fixedSetTime = when (isWorkTime) {
            null -> preparationTime
            true -> workTime
            false -> restTime
        }

        timer = object : CountDownTimer(totalTimeForLocalTimer, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                _isTimerOn.value = true
                _timerStage.value = TimerStages.RESUME
                //TODO перенести свичстейджес
                //switchStagesNames()
                //allows to resume to the timer with the same time left
                totalTimeForLocalTimer = millisUntilFinished

                updateLocalTime(millisUntilFinished)
                updateGlobalTime(millisUntilFinished)
                updateGlobalProgressIndicator(millisUntilFinished)
                //if (millisUntilFinished <= 3000) countDownPlayer.start()

            }

            override fun onFinish() {
                setsDone++
                _isTimerOn.value = false
                timePassed += fixedSetTime

                totalTimeForGlobalTimer -= fixedSetTime

                if (isWorkTime == false || isWorkTime == null) {
                    totalTimeForLocalTimer = workTime
                    isWorkTime = true

                } else {
                    totalTimeForLocalTimer = restTime
                    isWorkTime = false
                }

                if (setsDone == options.numberOfSets * 2) {
                    isWorkTime = null
                    _timerStage.value = TimerStages.RESTART
                }

                /* when (isWorkTime) {
                     true -> workTimePlayer.start()
                     false -> restTimePlayer.start()
                     null -> finishTimePlayer.start()
                 }*/

                if (setsDone == options.numberOfSets * 2) return


                startTimer()
            }

        }.start()
        _isTimerOn.value = true
    }


    fun pauseTimer() {
        if (_isTimerOn.value != true) return
        timer.cancel()
        _isTimerOn.value = false
        _timerStage.value = TimerStages.PAUSE
    }

    fun restartTimer() {
        pauseTimer()
        setValuesToDefault()
        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime)
    }

    private fun setValuesToDefault() {
        totalTimeForLocalTimer = preparationTime
        totalTimeForGlobalTimer = totalWorkoutTime
        fixedSetTime = preparationTime
        setsDone = -1
        isWorkTime = null
        _isTimerOn.value = null
        timePassed = 0
        _timerStage.value = TimerStages.PREPARATION
        _indicatorProgressValue.value = 0
    }

    private fun updateGlobalTime(millisUntilFinished: Long) {

        val timePassed = fixedSetTime - millisUntilFinished
        val totalSecondsLeft = ((totalTimeForGlobalTimer - timePassed) / 1000).toInt()//9000
        _globalTimeToShow.value = calculateTimeForUpdaters(totalSecondsLeft)
        //binding.generalTimerView.text = calculateTimeForTimersUpdaters(totalSecondsLeft)
    }

    private fun updateLocalTime(millisUntilFinished: Long) {
        val totalSecondsLeft = (millisUntilFinished / 1000).toInt()
        _localTimeToShow.value = calculateTimeForUpdaters(totalSecondsLeft)
        //binding.localTimerView.text = calculateTimeForTimersUpdaters(totalSecondsLeft)
    }

    private fun calculateTimeForUpdaters(totalSecondsLeft: Int): String {

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
            ((timePassed + fixedSetTime - (millisUntilFinished - onTickMethodCorrection)) / 1000).toDouble()

        _indicatorProgressValue.value = (timePassedSec / totalTimeSec * 100).toInt()
    }




    //надо отрефакторить
    /* private fun switchStagesNames() {

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

     }*/
}