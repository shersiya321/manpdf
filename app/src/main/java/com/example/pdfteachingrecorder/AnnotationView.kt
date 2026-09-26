package com.example.pdfteachingrecorder

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

enum class Tool { PEN, HIGHLIGHTER, ERASER }

class AnnotationView(context: Context) : View(context) {
    private val paths = mutableListOf<Pair<Path, Paint>>()
    var tool = Tool.PEN
    var strokeWidth = 8f
    private var current: Path? = null

    init { setLayerType(View.LAYER_TYPE_SOFTWARE, null) }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        for ((p, paint) in paths) c.drawPath(p, paint)
        current?.let { p ->
            val paint = makePaint()
            c.drawPath(p, paint)
        }
    }

    private fun makePaint(): Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        strokeWidth = if (tool == Tool.HIGHLIGHTER) 28f else strokeWidth
        color = if (tool == Tool.HIGHLIGHTER) Color.argb(90, 255, 193, 7) else Color.RED
        if (tool == Tool.ERASER) {
            color = Color.TRANSPARENT
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                current = Path().apply { moveTo(e.x, e.y) }
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                current?.lineTo(e.x, e.y); invalidate(); return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                current?.let { paths.add(it to makePaint()) }
                current = null; invalidate(); return true
            }
        }
        return true
    }

    fun undo() { if (paths.isNotEmpty()) { paths.removeLast(); invalidate() } }
    fun clearAll() { paths.clear(); invalidate() }
}
