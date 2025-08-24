package com.gabriaum.order.ui

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gabriaum.order.R
import kotlin.random.Random

class OrderActivity : AppCompatActivity() {

    private lateinit var tvMainMessage: TextView
    private lateinit var tvSecondaryMessage: TextView
    private lateinit var tvFinalMessage: TextView
    private lateinit var bigHeart: ImageView
    private lateinit var ringIcon: ImageView
    private lateinit var confettiContainer: ViewGroup

    private val handler = Handler(Looper.getMainLooper())

    private val mainMessage = "Olhe lentamente"
    private val secondaryMessage = "para trás"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_page)

        initViews()
        startAnimationSequence()
    }

    private fun initViews() {
        tvMainMessage = findViewById(R.id.tvMainMessage)
        tvSecondaryMessage = findViewById(R.id.tvSecondaryMessage)
        tvFinalMessage = findViewById(R.id.tvFinalMessage)
        bigHeart = findViewById(R.id.bigHeart)
        ringIcon = findViewById(R.id.ringIcon)
        confettiContainer = findViewById(R.id.confettiContainer)
    }

    private fun startAnimationSequence() {
        startConfetti()

        handler.postDelayed({
            animateHeartEntrance()
        }, 500)

        handler.postDelayed({
            typeWriterEffect(tvMainMessage, mainMessage) {
                handler.postDelayed({
                    typeWriterEffect(tvSecondaryMessage, secondaryMessage)
                }, 500)
            }
        }, 1500)

        handler.postDelayed({
            animateFloatingElements()
        }, 2000)
    }

    private fun startConfetti() {
        val colors = arrayOf("#FF69B4", "#FFB6C1", "#FF1493", "#DC143C", "#FFD700", "#FFA500")
        val emojis = arrayOf("🎉", "✨", "💖", "💕", "🌟", "💫", "🎊")

        val confettiRunnable = object : Runnable {
            override fun run() {
                repeat(3) {
                    createConfettiPiece(colors.random(), emojis.random())
                }
                handler.postDelayed(this, 300)
            }
        }
        handler.post(confettiRunnable)
    }

    private fun createConfettiPiece(color: String, emoji: String) {
        val confetti = TextView(this).apply {
            text = emoji
            textSize = Random.nextInt(16, 28).toFloat()
            setTextColor(Color.parseColor(color))
            x = Random.nextInt(0, confettiContainer.width).toFloat()
            y = -100f
        }

        confettiContainer.addView(confetti)

        // Animação de queda
        val fallAnimator = ObjectAnimator.ofFloat(confetti, "translationY", -100f, confettiContainer.height + 100f)
        val rotationAnimator = ObjectAnimator.ofFloat(confetti, "rotation", 0f, 360f * 3)
        val scaleAnimator = ObjectAnimator.ofFloat(confetti, "scaleX", 1f, 0.5f)
        val scaleYAnimator = ObjectAnimator.ofFloat(confetti, "scaleY", 1f, 0.5f)

        val animatorSet = AnimatorSet().apply {
            playTogether(fallAnimator, rotationAnimator, scaleAnimator, scaleYAnimator)
            duration = Random.nextLong(2000, 4000)
        }

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                confettiContainer.removeView(confetti)
            }
        })

        animatorSet.start()
    }

    private fun animateHeartEntrance() {
        bigHeart.alpha = 0f
        bigHeart.scaleX = 0f
        bigHeart.scaleY = 0f

        val scaleAnimator = ValueAnimator.ofFloat(0f, 1.2f, 1f)
        scaleAnimator.duration = 800
        scaleAnimator.addUpdateListener { animator ->
            val scale = animator.animatedValue as Float
            bigHeart.scaleX = scale
            bigHeart.scaleY = scale
        }

        val alphaAnimator = ObjectAnimator.ofFloat(bigHeart, "alpha", 0f, 1f)
        alphaAnimator.duration = 600

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleAnimator, alphaAnimator)
        animatorSet.start()

        handler.postDelayed({
            startHeartBeat()
        }, 1000)
    }

    private fun startHeartBeat() {
        val beatAnimator = ValueAnimator.ofFloat(1f, 1.1f, 1f)
        beatAnimator.duration = 1000
        beatAnimator.repeatCount = ValueAnimator.INFINITE
        beatAnimator.addUpdateListener { animator ->
            val scale = animator.animatedValue as Float
            bigHeart.scaleX = scale
            bigHeart.scaleY = scale
        }
        beatAnimator.start()
    }

    private fun typeWriterEffect(textView: TextView, text: String, onComplete: (() -> Unit)? = null) {
        textView.alpha = 1f
        textView.text = ""

        var currentIndex = 0
        val typeRunnable = object : Runnable {
            override fun run() {
                if (currentIndex <= text.length) {
                    textView.text = text.substring(0, currentIndex)
                    currentIndex++
                    handler.postDelayed(this, 100)
                } else {
                    onComplete?.invoke()
                }
            }
        }
        handler.post(typeRunnable)
    }

    private fun animateFloatingElements() {
        val heartIds = arrayOf(R.id.floatingHeart1, R.id.floatingHeart2, R.id.floatingHeart3, R.id.floatingHeart4, R.id.floatingHeart5)
        heartIds.forEachIndexed { index, heartId ->
            handler.postDelayed({
                animateFloatingHeart(findViewById(heartId))
            }, index * 200L)
        }

        val starIds = arrayOf(R.id.star1, R.id.star2, R.id.star3, R.id.star4)
        starIds.forEachIndexed { index, starId ->
            handler.postDelayed({
                animateStarTwinkle(findViewById(starId))
            }, index * 300L)
        }
    }

    private fun animateFloatingHeart(heart: ImageView) {
        // Aparição
        val alphaAnimator = ObjectAnimator.ofFloat(heart, "alpha", 0f, 1f)
        alphaAnimator.duration = 500
        alphaAnimator.start()

        // Movimento flutuante
        val floatUpAnimator = ObjectAnimator.ofFloat(heart, "translationY", 0f, -50f, 0f, 50f, 0f)
        floatUpAnimator.duration = 4000
        floatUpAnimator.repeatCount = ValueAnimator.INFINITE

        val scaleAnimator = ObjectAnimator.ofFloat(heart, "scaleX", 1f, 1.2f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(heart, "scaleY", 1f, 1.2f, 1f)
        scaleAnimator.duration = 2000
        scaleYAnimator.duration = 2000
        scaleAnimator.repeatCount = ValueAnimator.INFINITE
        scaleYAnimator.repeatCount = ValueAnimator.INFINITE

        floatUpAnimator.start()
        scaleAnimator.start()
        scaleYAnimator.start()
    }

    private fun animateStarTwinkle(star: TextView) {
        // Efeito de piscar das estrelas
        val twinkleAnimator = ObjectAnimator.ofFloat(star, "alpha", 0f, 1f, 0f)
        twinkleAnimator.duration = 1500
        twinkleAnimator.repeatCount = ValueAnimator.INFINITE
        twinkleAnimator.startDelay = Random.nextLong(0, 1000)

        val scaleAnimator = ObjectAnimator.ofFloat(star, "scaleX", 0.8f, 1.3f, 0.8f)
        val scaleYAnimator = ObjectAnimator.ofFloat(star, "scaleY", 0.8f, 1.3f, 0.8f)
        scaleAnimator.duration = 1500
        scaleYAnimator.duration = 1500
        scaleAnimator.repeatCount = ValueAnimator.INFINITE
        scaleYAnimator.repeatCount = ValueAnimator.INFINITE

        twinkleAnimator.start()
        scaleAnimator.start()
        scaleYAnimator.start()
    }
}