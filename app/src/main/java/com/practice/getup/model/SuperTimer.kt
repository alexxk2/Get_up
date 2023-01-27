package com.practice.getup.model


import com.practice.getup.interfaces.AdvancedTimer

abstract class SuperTimer : AdvancedTimer {

    abstract override fun start()

    abstract override fun pauseTimer()

    abstract override fun restartTimer()

}