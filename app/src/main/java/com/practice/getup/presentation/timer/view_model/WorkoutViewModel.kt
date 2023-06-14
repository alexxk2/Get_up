package com.practice.getup.presentation.timer.view_model

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practice.getup.R
import com.practice.getup.models.Stage
import com.practice.getup.presentation.timer.models.TimerStages
import com.practice.getup.utils.UiText
import com.practice.getup.database.Workout
import com.practice.getup.presentation.timer.models.SoundStages
import java.lang.IllegalArgumentException

class WorkoutViewModel(private val workout: Workout) : ViewModel() {

    private lateinit var timer: CountDownTimer

    private val workTime = (workout.workTime * 1000).toLong()
    private val restTime = (workout.restTime * 1000).toLong()
    private val preparationTime = (workout.preparingTime * 1000).toLong()
    private val numberOfSets = (workout.numberOfSets)
    private val totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime

    private var totalTimeForLocalTimer: Long = preparationTime
    private var totalTimeForGlobalTimer: Long = totalWorkoutTime
    private var fixedSetTime = preparationTime
    private var setsDone = -1
    private var isWorkTime: Boolean? = null
    private var timePassed: Long = 0
    private var isTimerOn: Boolean = false

    private val _timerStage = MutableLiveData(TimerStages.PREPARATION)
    val timerStage: LiveData<TimerStages> = _timerStage

    private val _localTimeToShow = MutableLiveData("")
    val localTimeToShow: LiveData<String> = _localTimeToShow

    private val _globalTimeToShow = MutableLiveData("")
    val globalTimeToShow: LiveData<String> = _globalTimeToShow

    private val _indicatorProgressValue = MutableLiveData(0)
    val indicatorProgressValue: LiveData<Int> = _indicatorProgressValue

    private val _stageList = MutableLiveData<MutableList<Stage>>()
    val stageList: LiveData<MutableList<Stage>> = _stageList

    private val _currentStagePosition = MutableLiveData(numberOfSets * 2 + 3)
    val currentStagePosition: LiveData<Int> = _currentStagePosition

    private val _soundStage = MutableLiveData<SoundStages>()
    val soundStages: LiveData<SoundStages> = _soundStage

    init {
        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime)
        createListOfStages(_currentStagePosition.value ?: (numberOfSets * 2 + 3))
    }

    fun startTimer() {

        if (isTimerOn) return

        fixedSetTime = when (isWorkTime) {
            null -> preparationTime
            true -> workTime
            false -> restTime
        }

        _timerStage.value = TimerStages.RESUME

        timer = object : CountDownTimer(totalTimeForLocalTimer, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                isTimerOn = true

                //allows to resume to the timer with the same time left
                totalTimeForLocalTimer = millisUntilFinished

                updateLocalTime(millisUntilFinished)
                updateGlobalTime(millisUntilFinished)
                updateGlobalProgressIndicator(millisUntilFinished)
                if (millisUntilFinished <= 3000) _soundStage.value = SoundStages.COUNTDOWN
            }

            override fun onFinish() {
                setsDone++
                isTimerOn = false
                timePassed += fixedSetTime
                _currentStagePosition.value = _currentStagePosition.value!! - 1
                totalTimeForGlobalTimer -= fixedSetTime

                removeLastStage()
                updateFocus()

                if (isWorkTime == false || isWorkTime == null) {
                    totalTimeForLocalTimer = workTime
                    isWorkTime = true

                } else {
                    totalTimeForLocalTimer = restTime
                    isWorkTime = false
                }

                if (setsDone == workout.numberOfSets * 2) {
                    isWorkTime = null
                    _timerStage.value = TimerStages.RESTART
                }

                 when (isWorkTime) {
                     true -> _soundStage.value = SoundStages.WORK
                     false -> _soundStage.value = SoundStages.REST
                     null -> _soundStage.value = SoundStages.FINISH
                 }

                if (setsDone == workout.numberOfSets * 2) return

                startTimer()
            }
        }.start()
        isTimerOn = true
    }


    fun pauseTimer() {
        if (!isTimerOn) return
        timer.cancel()
        isTimerOn = false
        _timerStage.value = TimerStages.PAUSE
    }

    fun restartTimer() {
        pauseTimer()
        setValuesToDefault()
        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime)
        createListOfStages(_currentStagePosition.value ?: (numberOfSets * 2 + 3))
    }

    private fun setValuesToDefault() {
        totalTimeForLocalTimer = preparationTime
        totalTimeForGlobalTimer = totalWorkoutTime
        fixedSetTime = preparationTime
        setsDone = -1
        isWorkTime = null
        isTimerOn = false
        timePassed = 0
        _timerStage.value = TimerStages.PREPARATION
        _indicatorProgressValue.value = 0
        _currentStagePosition.value = numberOfSets * 2 + 3
    }

    private fun updateGlobalTime(millisUntilFinished: Long) {

        val timePassed = fixedSetTime - millisUntilFinished
        val totalSecondsLeft = ((totalTimeForGlobalTimer - timePassed) / 1000).toInt()//9000
        _globalTimeToShow.value = calculateTimeForUpdaters(totalSecondsLeft)
    }

    private fun updateLocalTime(millisUntilFinished: Long) {
        val totalSecondsLeft = (millisUntilFinished / 1000).toInt()
        _localTimeToShow.value = calculateTimeForUpdaters(totalSecondsLeft)
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

    private fun createListOfStages(positionInFocus: Int) {
        val ready = UiText.StringResource(R.string.ready_text)
        val work = UiText.StringResource(R.string.work_text)
        val rest = UiText.StringResource(R.string.rest_text)
        val blank = UiText.StringResource(R.string.blank)
        val emptyString = UiText.StringResource(R.string.empty_string)

        val listOfStages = mutableListOf<Stage>(
            Stage("${blank}1", emptyString, emptyString, false),
            Stage("${blank}2", emptyString, emptyString, false),
            Stage("${blank}3", emptyString, emptyString, false)
        )

        for (n in 1..numberOfSets) {
            listOfStages.add(Stage("$rest$n", rest, emptyString, false))
            listOfStages.add(Stage("$work$n", work, UiText.StringResource(R.string.sets_left, n),false))
        }
        listOfStages.add(Stage("$ready$numberOfSets", ready, emptyString, true))

        _stageList.value = listOfStages
    }

    private fun removeLastStage() {

        val tempList = mutableListOf<Stage>()
        _stageList.value?.let { tempList.addAll(it) }
        tempList.removeLast()
        _stageList.value = tempList

    }
    private fun updateFocus(){

        val tempList = mutableListOf<Stage>()
        _stageList.value?.let { tempList.addAll(it) }

        val updatedStage = tempList.last().copy(hasFocus = true)
        tempList[tempList.size - 1] = updatedStage
        _stageList.value = tempList
    }

}

class ViewModelFactoryFragments(private val workout: Workout) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(workout) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }


}
