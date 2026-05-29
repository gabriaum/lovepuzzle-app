package com.gabriaum.app.service

import android.os.CountDownTimer
import com.gabriaum.app.backend.data.ExpireData

class DailyLimitService(
    private val expireData: ExpireData,
    private val limit: Int
) {
    private var countDownTimer: CountDownTimer? = null

    fun check(listener: DailyLimitListener) {
        if (!expireData.availableToContinue(limit)) {
            val remainingTime = expireData.getTime()
            listener.onLimitActive(remainingTime)
            startTimer(remainingTime, listener)
        } else {
            listener.onAvailable()
        }
    }

    private fun startTimer(remainingTime: Long, listener: DailyLimitListener) {
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / 1000 / 3600
                val minutes = (millisUntilFinished / 1000 % 3600) / 60
                val seconds = millisUntilFinished / 1000 % 60
                listener.onTimerTick(hours, minutes, seconds)
            }

            override fun onFinish() {
                expireData.resetLevelProgressed()
                listener.onLimitExpired()
                listener.onAvailable()
            }
        }.start()
    }

    fun cancel() {
        countDownTimer?.cancel()
        countDownTimer = null
    }
}