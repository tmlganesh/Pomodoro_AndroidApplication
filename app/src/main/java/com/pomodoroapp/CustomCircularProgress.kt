package com.pomodoroapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomCircularProgress(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint: Paint = Paint()
    private var progress: Float = 0f // Progress from 0 to 1
    private val maxProgress = 100f
    private val strokeWidth = 20f

    init {
        paint.apply {
            isAntiAlias = true
            color = Color.parseColor("#4CAF50") // Green color
            style = Paint.Style.STROKE
            this.strokeWidth = this@CustomCircularProgress.strokeWidth
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Ensure the circular progress view is round by adjusting to the smallest dimension
        val size = Math.min(w, h)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Get the size of the view (it will always be square after onSizeChanged)
        val width = width.toFloat()
        val height = height.toFloat()

        // Calculate radius as half of the smallest dimension
        val radius = (Math.min(width, height) - strokeWidth) / 2

        // Draw the background circle (light gray)
        paint.color = Color.parseColor("#E0E0E0")
        canvas.drawCircle(width / 2, height / 2, radius, paint)

        // Draw the progress circle
        paint.color = Color.parseColor("#4CAF50") // Green color
        val sweepAngle = 360 * (progress / maxProgress)
        canvas.drawArc(
            strokeWidth, strokeWidth, width - strokeWidth, height - strokeWidth,
            -90f, sweepAngle, false, paint
        )
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate() // Redraw the view
    }
}
