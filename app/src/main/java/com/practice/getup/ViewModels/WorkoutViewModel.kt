package com.practice.getup.ViewModels

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practice.getup.R
import com.practice.getup.model.Options
import com.practice.getup.model.Stage
import com.practice.getup.model.TimerStages
import com.practice.getup.activities.UiText

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
    private var isTimerOn: Boolean = false


    private val _timerStage = MutableLiveData(TimerStages.PREPARATION)
    val timerStage: LiveData<TimerStages> = _timerStage

    private val _localTimeToShow = MutableLiveData("")
    val localTimeToShow: LiveData<String> = _localTimeToShow

    private val _globalTimeToShow = MutableLiveData("")
    val globalTimeToShow: LiveData<String> = _globalTimeToShow

    private val _indicatorProgressValue = MutableLiveData<Int>(0)
    val indicatorProgressValue: LiveData<Int> = _indicatorProgressValue

    private val _stageList = MutableLiveData<MutableList<Stage>>()
    val stageList: LiveData<MutableList<Stage>> = _stageList

    private val _currentStagePosition = MutableLiveData(numberOfSets * 2)
    val currentStagePosition: LiveData<Int> = _currentStagePosition

    init {
        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime)
        //_indicatorProgressValue.value = 0
        createListOfStages(_currentStagePosition.value ?: (numberOfSets * 2))
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
                isTimerOn = false
                timePassed += fixedSetTime
                _currentStagePosition.value = _currentStagePosition.value!! - 1
                totalTimeForGlobalTimer -= fixedSetTime
                //createListOfStages(_currentStagePosition.value ?: (numberOfSets * 2))
                //changeFocus(_currentStagePosition.value?: 0)

                removeLastStage()

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
        //changeFocus(_currentStagePosition.value ?: 0)
        createListOfStages(_currentStagePosition.value ?: (numberOfSets * 2))
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
        _currentStagePosition.value = numberOfSets * 2
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

    private fun createListOfStages(positionInFocus: Int){
        val ready = UiText.StringResource(R.string.ready_text)
        val work = UiText.StringResource(R.string.work_text)
        val rest = UiText.StringResource(R.string.rest_text)
        val emptyString = UiText.StringResource(R.string.empty_string)

        val listOfStages = mutableListOf<Stage>()

        for (n in 1..numberOfSets) {
            listOfStages.add(Stage(n + 1, rest, emptyString, false))
            listOfStages.add(Stage(n + 2, work, UiText.StringResource(R.string.sets_left, n), false))
        }
        listOfStages.add(Stage(numberOfSets + 3, ready, emptyString, false))

        listOfStages[positionInFocus].hasFocus = true

        _stageList.value = listOfStages
    }

    private fun removeLastStage(){

        val tempList = mutableListOf<Stage>()
        _stageList.value?.forEach {
            tempList.add(it)
        }
        tempList.removeLast()
        tempList[tempList.size-1].hasFocus = true
        _stageList.value = tempList
    }



 /*   private fun changeFocus(positionInFocus: Int) {

        val tempStageList = createListOfStages()
        tempStageList[positionInFocus].hasFocus = true

        if (positionInFocus != tempStageList.lastIndex) {
            tempStageList[positionInFocus + 1].hasFocus = false
        }
        _stageList.value = tempStageList

        *//* if (positionInFocus >= 0) {

             val elementOnFocus = _stageList.value?.get(positionInFocus) ?: return
             elementOnFocus.hasFocus = true


             if (positionInFocus != _stageList.value!!.size) {
                 val elementOutOfFocus = _stageList.value?.get(positionInFocus + 1) ?: return
                 elementOutOfFocus.hasFocus = false
             }
         }*//*

    }*/


}