package com.gabriaum.order.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.gabriaum.order.R
import android.graphics.drawable.GradientDrawable
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.core.graphics.toColorInt

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val rootLayout = findViewById<RelativeLayout>(R.id.rootLayout)
        val startColors = intArrayOf("#8e2de2".toColorInt(), "#4a00e0".toColorInt())
        val endColors = intArrayOf("#00f260".toColorInt(), "#0575e6".toColorInt())
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            startColors
        )
        gradient.cornerRadius = 0f
        rootLayout.background = gradient

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 5000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedValue as Float

            val color0 = ArgbEvaluator().evaluate(fraction, startColors[0], endColors[0]) as Int
            val color1 = ArgbEvaluator().evaluate(fraction, startColors[1], endColors[1]) as Int

            gradient.colors = intArrayOf(color0, color1)
        }
        animator.start()

        val heart = findViewById<ImageView>(R.id.heartImage)
        val animation = AnimationUtils.loadAnimation(this, R.anim.heart_pulse)
        heart.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}