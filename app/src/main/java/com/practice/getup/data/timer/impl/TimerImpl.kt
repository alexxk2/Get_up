package com.practice.getup.data.timer.impl

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import com.practice.getup.R
import com.practice.getup.data.db.dto.WorkoutDto
import com.practice.getup.data.timer.Timer
import com.practice.getup.data.timer.dto.StageDto
import com.practice.getup.data.timer.dto.TimerStagesDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerImpl(
    private val context: Context,
): Timer {

    private lateinit var  workoutDto: WorkoutDto
    private lateinit var timer: CountDownTimer

    private var workTime: Long = 0
    private var restTime: Long = 0
    private var preparationTime: Long = 0
    private var numberOfSets: Int = 0
    private var totalWorkoutTime: Long = 0
    private var totalTimeForLocalTimer: Long = 0
    private var totalTimeForGlobalTimer: Long = 0
    private var fixedSetTime: Long = 0

    private var setsDone = -1
    private var isWorkTime: Boolean? = null
    private var timePassed: Long = 0
    private var isTimerOn: Boolean = false
    private var mediaPlayer: MediaPlayer? = null

    private val timerStage = MutableStateFlow(TimerStagesDto.PREPARATION)
    private val localTimeToShow = MutableStateFlow("")
    private val globalTimeToShow = MutableStateFlow("")
    private val indicatorProgressValue = MutableStateFlow(0)
    private val stageList = MutableStateFlow(mutableListOf(StageDto.DEFAULT))
    private val currentStagePosition = MutableStateFlow(numberOfSets * 2 + 3)


    override fun startTimer() {
        if (isTimerOn) return

        fixedSetTime = when (isWorkTime) {
            null -> preparationTime
            true -> workTime
            false -> restTime
        }

        timerStage.value = TimerStagesDto.RESUME

        timer = object : CountDownTimer(totalTimeForLocalTimer, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                isTimerOn = true
                //allows to resume to the timer with the same time left
                totalTimeForLocalTimer = millisUntilFinished

                updateLocalTime(millisUntilFinished)
                updateGlobalTime(millisUntilFinished)
                updateGlobalProgressIndicator(millisUntilFinished)
                makeCountdownSound(millisUntilFinished)

            }

            override fun onFinish() {
                setsDone++
                isTimerOn = false
                timePassed += fixedSetTime
                currentStagePosition.value  -= 1
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

                if (setsDone == workoutDto.numberOfSets * 2) {
                    isWorkTime = null
                    timerStage.value = TimerStagesDto.RESTART
                }

                when (isWorkTime) {
                    true -> makeTimerSound(R.raw.sound_work_start)
                    false -> makeTimerSound(R.raw.sound_rest_start)
                    null -> makeTimerSound(R.raw.sound_workout_finish)
                }

                if (setsDone == workoutDto.numberOfSets * 2) {
                    mediaPlayer?.setOnCompletionListener {
                        mediaPlayer?.release()
                        mediaPlayer = null
                    }
                    return
                }
                startTimer()
            }
        }.start()
        isTimerOn = true
    }

    override fun pauseTimer() {
        if (!isTimerOn) return
        timer.cancel()
        isTimerOn = false
        timerStage.value = TimerStagesDto.PAUSE
    }

    override fun restartTimer() {
        pauseTimer()
        setValuesToDefault()
        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime)
        createListOfStages()
    }

    private fun makeCountdownSound(millisUntilFinished: Long){
        if (millisUntilFinished <= 3000) makeTimerSound(R.raw.sound_countdown)
        if (millisUntilFinished <= 2000) makeTimerSound(R.raw.sound_countdown)
        if (millisUntilFinished <= 1000) makeTimerSound(R.raw.sound_countdown)
    }

    private fun setValuesToDefault() {
        totalTimeForLocalTimer = preparationTime
        totalTimeForGlobalTimer = totalWorkoutTime
        fixedSetTime = preparationTime
        setsDone = -1
        isWorkTime = null
        isTimerOn = false
        timePassed = 0
        timerStage.value = TimerStagesDto.PREPARATION
        indicatorProgressValue.value = 0
        currentStagePosition.value = numberOfSets * 2 + 3
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun updateGlobalTime(millisUntilFinished: Long) {

        val timePassed = fixedSetTime - millisUntilFinished
        val totalSecondsLeft = ((totalTimeForGlobalTimer - timePassed) / 1000).toInt()
        globalTimeToShow.value = calculateTimeForUpdaters(totalSecondsLeft)
    }

    private fun updateLocalTime(millisUntilFinished: Long) {
        val totalSecondsLeft = (millisUntilFinished / 1000).toInt()
        localTimeToShow.value = calculateTimeForUpdaters(totalSecondsLeft)
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

        indicatorProgressValue.value = (timePassedSec / totalTimeSec * 100).toInt()
    }

    private fun createListOfStages() {
        val ready = context.getString(R.string.ready_text)
        val work = context.getString(R.string.work_text)
        val rest = context.getString(R.string.rest_text)
        val blank = context.getString(R.string.blank)
        val emptyString = context.getString(R.string.empty_string)

        val listOfStages = mutableListOf<StageDto>(
            StageDto("${blank}1", emptyString, emptyString, false),
            StageDto("${blank}2", emptyString, emptyString, false),
            StageDto("${blank}3", emptyString, emptyString, false)
        )

        for (n in 1..numberOfSets) {
            listOfStages.add(StageDto("$rest$n", rest, emptyString, false))
            listOfStages.add(StageDto("$work$n", work, context.getString(R.string.sets_left, n),false))
        }
        listOfStages.add(StageDto("$ready$numberOfSets", ready, emptyString, true))

        stageList.value = listOfStages
    }

    private fun removeLastStage() {
        val tempList = mutableListOf<StageDto>()
        stageList.value.let { tempList.addAll(it) }
        tempList.removeLast()
        stageList.value = tempList
    }

    private fun updateFocus(){

        val tempList = mutableListOf<StageDto>()
        stageList.value.let { tempList.addAll(it) }

        val updatedStage = tempList.last().copy(hasFocus = true)
        tempList[tempList.size - 1] = updatedStage
        stageList.value = tempList

    }

    private fun makeTimerSound(soundRes: Int){
        mediaPlayer?.release()
        mediaPlayer = null
        mediaPlayer = MediaPlayer.create(context, soundRes)
        mediaPlayer?.start()
    }

    override fun prepareTimer(workoutDto: WorkoutDto) {
        this.workoutDto = workoutDto
        workTime = (workoutDto.workTime * 1000).toLong()
        restTime = (workoutDto.restTime * 1000).toLong()
        preparationTime = (workoutDto.preparingTime * 1000).toLong()
        numberOfSets = workoutDto.numberOfSets
        totalWorkoutTime = (workTime + restTime) * numberOfSets + preparationTime

        totalTimeForLocalTimer = preparationTime
        totalTimeForGlobalTimer = totalWorkoutTime
        fixedSetTime = preparationTime
        currentStagePosition.value = numberOfSets * 2 + 3

        updateLocalTime(preparationTime)
        updateGlobalTime(preparationTime)
        createListOfStages()
    }

    override fun getWorkoutStagePosition(): Flow<Int> = currentStagePosition

    override fun getGlobalTime(): Flow<String> = globalTimeToShow

    override fun getIndicatorProgressValue(): Flow<Int> = indicatorProgressValue

    override fun getLocalTime(): Flow<String> = localTimeToShow

    override fun getStageList(): StateFlow<MutableList<StageDto>> = stageList

    override fun getTimerStage(): Flow<TimerStagesDto>  = timerStage

}