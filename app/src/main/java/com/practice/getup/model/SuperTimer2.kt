package com.practice.getup.model

import android.os.CountDownTimer
import com.practice.getup.interfaces.AdvancedTimer

abstract class SuperTimer2(workoutOptions: Options) : AdvancedTimer {

    abstract fun start()

    abstract override fun pauseTimer()

    abstract override fun restartTimer()

}