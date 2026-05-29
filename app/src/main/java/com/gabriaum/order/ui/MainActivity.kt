package com.gabriaum.order.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.TextKeyListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.gabriaum.order.R
import com.gabriaum.order.backend.data.AccountData
import com.gabriaum.order.backend.data.ExpireData
import com.gabriaum.order.backend.data.impl.AccountDataImpl
import com.gabriaum.order.backend.data.impl.ExpireDataImpl
import com.gabriaum.order.backend.database.sql.SQLConnection
import com.gabriaum.order.domain.controller.ResponseManager
import com.gabriaum.order.domain.model.Response
import com.gabriaum.order.domain.service.DailyLimitListener
import com.gabriaum.order.domain.service.DailyLimitService
import com.gabriaum.order.domain.service.WebhookService
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity(), DailyLimitListener {
    private lateinit var sqlConnection: SQLConnection
    private lateinit var accountData: AccountData
    private lateinit var expireData: ExpireData
    private val responseManager = ResponseManager()
    private lateinit var unlockButton: Button
    private var currentDailyLimitService: DailyLimitService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqlConnection = SQLConnection(this)
        sqlConnection.connect()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launchWhenCreated {
            accountData = AccountDataImpl(sqlConnection.getDatabase())
            if (!accountData.exists())
                accountData.register( 0)

            expireData = ExpireDataImpl(sqlConnection.getDatabase())
            if (!expireData.exists())
                expireData.register()

            unlockButton = findViewById(R.id.unlockButton)
            currentDailyLimitService = DailyLimitService(expireData, 5)
            currentDailyLimitService!!.check(this@MainActivity)

            loadQuestion()
            loadProgressBar()
        }

        extraComponentsClickable()
    }

    private fun loadProgressBar() {
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val level: Int = accountData.getLevel() + 1
        val levels: Int = responseManager.size
        progressBar.progress = level
        progressBar.max = levels

        val stage: TextView = findViewById(R.id.tvStage)
        stage.text = String.format("Your progress (%s/%s)", level, levels)
    }

    private fun clickableButton() {
        val button: Button = findViewById(R.id.unlockButton)
        val expectedResponseInButton: EditText = findViewById(R.id.etAnswer)

        expectedResponseInButton.keyListener = TextKeyListener.getInstance()
        expectedResponseInButton.filters = arrayOf()

        button.setOnClickListener {
            val level: Int = accountData.getLevel()
            val response: Response = responseManager[level]
            val userAnswer: String =
                expectedResponseInButton.text.toString().toLowerCase(Locale.ROOT)
            if (!userAnswer.isEmpty()) {
                if (userAnswer.contains(response.expected)) {
                    accountData.upLevel()
                    WebhookService().sendDiscordWebhook(
                        "",
                        "> New level reached:  **" + (accountData.getLevel() + 1) + "**."
                    )
                    if ((level + 1) >= responseManager.size) {
                        val intent = Intent(this@MainActivity, OrderActivity::class.java)
                        startActivity(intent)
                    } else {
                        expectedResponseInButton.text.clear()
                        loadQuestion()
                        loadProgressBar()
                    }
                } else {
                    WebhookService().sendDiscordWebhook(
                        "",
                        "> Wrong level **" + level + "**... (``" + userAnswer + "``, expected response: ``" + response.expected + "``)"
                    )
                }

                expireData.addLevelProgressed()
                if (expireData.getProgressedLevels() >= 5) {
                    expireData.blockForOneDay()
                    currentDailyLimitService = DailyLimitService(expireData, 5)
                    currentDailyLimitService!!.check(this@MainActivity)
                } else {
                    val attemptsLeft = 5 - expireData.getProgressedLevels()
                    Toast.makeText(
                        this,
                        "You have $attemptsLeft attempts remaining",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun extraComponentsClickable() {
        val backArrow: ImageView = findViewById(R.id.btnBack)
        backArrow.setOnClickListener {
            finish()
        }

        val githubIcon: ImageView = findViewById(R.id.ic_github)
        githubIcon.setOnClickListener {
            val url = "https://github.com/gabriaum"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        val linkedinIcon: ImageView = findViewById(R.id.ic_linkedin)
        linkedinIcon.setOnClickListener {
            val url = "https://www.linkedin.com/in/gabriel-bruck-140b42352/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadQuestion() {
        val question: TextView = findViewById(R.id.tvQuestion)
        val questionNumber: TextView = findViewById(R.id.tvQuestionNumber)
        val level: Int = accountData.getLevel()
        if (level >= responseManager.size) {
            val intent = Intent(this@MainActivity, OrderActivity::class.java)
            startActivity(intent)
        } else {
            val response: Response = responseManager[level]
            question.text = response.question
            questionNumber.text = (level + 1).toString()
        }
    }

    override fun onLimitActive(remainingTimeMs: Long) {
        unlockButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
        unlockButton.isEnabled = false
    }

    override fun onTimerTick(hours: Long, minutes: Long, seconds: Long) {
        unlockButton.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onLimitExpired() {
        unlockButton.setBackgroundColor(ContextCompat.getColor(this, R.color.originalButtonColor))
        unlockButton.text = "Check"
    }

    override fun onAvailable() {
        unlockButton.isEnabled = true
        clickableButton()
    }
}