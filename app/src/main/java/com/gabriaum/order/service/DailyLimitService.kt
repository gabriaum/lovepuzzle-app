package com.gabriaum.order.domain.service

import android.os.CountDownTimer
import android.widget.Button
import androidx.core.content.ContextCompat
import com.gabriaum.order.backend.data.ExpireData
import com.gabriaum.order.R
import kotlin.math.exp

class DailyLimitService(
    private val expireData: ExpireData,
    private val limit: Int
) {
    fun applyToButton(button: Button, onAvailable: () -> Unit) {
        if (!expireData.availableToContinue(limit)) {
            val remainingTime = expireData.getTime()
            button.setBackgroundColor(ContextCompat.getColor(button.context, android.R.color.holo_red_dark))
            button.isEnabled = false

            object : CountDownTimer(remainingTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = millisUntilFinished / 1000 / 3600
                    val minutes = (millisUntilFinished / 1000 % 3600) / 60
                    val seconds = millisUntilFinished / 1000 % 60
                    button.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                }

                override fun onFinish() {
                    expireData.resetLevelProgressed()
                    button.setBackgroundColor(ContextCompat.getColor(button.context, R.color.originalButtonColor))
                    button.text = "Check"
                    button.isEnabled = true
                    onAvailable()
                }
            }.start()
        } else {
            onAvailable()
        }
    }
}