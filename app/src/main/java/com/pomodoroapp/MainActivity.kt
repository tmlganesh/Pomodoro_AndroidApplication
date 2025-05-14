package com.pomodoroapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class MainActivity : AppCompatActivity() {

    private lateinit var startButton: Button
    private lateinit var resetButton: ImageButton
    private lateinit var timerText: TextView
    private lateinit var pomodoroCountText: TextView
    private lateinit var circularProgress: CustomCircularProgress

    private var timer: CountDownTimer? = null
    private var isRunning = false
    private var pomodoroCount = 0
    private val workDuration: Long = 25 * 60 * 1000 // 25 minutes in milliseconds
    private var timeLeftInMillis: Long = workDuration
    private val interval: Long = 1000 // 1 second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        setContentView(R.layout.activity_main)

        val rootView = findViewById<RelativeLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startButton = findViewById(R.id.startButton)
        resetButton = findViewById(R.id.resetButton)
        timerText = findViewById(R.id.timerText)
        pomodoroCountText = findViewById(R.id.pomodoroCountText)
        circularProgress = findViewById(R.id.circularProgress)

        startButton.setOnClickListener { onStartStopClicked() }
        resetButton.setOnClickListener { onResetClicked() }

        updateTimerUI()
    }

    private fun onStartStopClicked() {
        if (isRunning) {
            timer?.cancel()
            isRunning = false
            startButton.text = "START"
        } else {
            startTimer()
            isRunning = true
            startButton.text = "PAUSE"
        }
    }

    private fun onResetClicked() {
        timer?.cancel()
        timeLeftInMillis = workDuration
        isRunning = false
        startButton.text = "START"
        updateTimerUI()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerUI()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                pomodoroCount++
                updateTimerUI()
                updatePomodoroCount()
            }
        }.start()
    }

    private fun updateTimerUI() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeText = String.format("%02d:%02d", minutes, seconds)
        timerText.text = timeText

        val progress = (workDuration - timeLeftInMillis) / (workDuration / 100f)
        circularProgress.setProgress(progress)
    }

    private fun updatePomodoroCount() {
        pomodoroCountText.text = "Pomodoro #$pomodoroCount"
    }
}
