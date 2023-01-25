package com.practice.getup.model

import android.os.CountDownTimer
import com.practice.getup.interfaces.AdvancedTimer

abstract class SuperTimer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(
    millisInFuture,
    countDownInterval
), AdvancedTimer {

    abstract override fun onTick(millisUntilFinished: Long)

    abstract override fun onFinish()

    abstract override fun pauseTimer()

    abstract override fun restartTimer()

}