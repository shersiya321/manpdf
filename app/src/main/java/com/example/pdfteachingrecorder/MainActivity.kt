package com.example.pdfteachingrecorder

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import java.io.ParcelFileDescriptor

class MainActivity : Activity() {
    private lateinit var pdfImage: ImageView
    private lateinit var annotations: AnnotationView
    private lateinit var pageLabel: TextView
    private lateinit var toolbar: LinearLayout
    private lateinit var toolbarParams: FrameLayout.LayoutParams
    private var renderer: PdfRenderer? = null
    private var pfd: ParcelFileDescriptor? = null
    private var pageIndex = 0
    private val pickPdf = 10

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)
        pdfImage = findViewById(R.id.pdfImage)
        annotations = findViewById(R.id.annotations)
        pageLabel = findViewById(R.id.pageLabel)
        toolbar = findViewById(R.id.toolbar)
        toolbarParams = toolbar.layoutParams as FrameLayout.LayoutParams

        findViewById<Button>(R.id.openPdf).setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "application/pdf"; addCategory(Intent.CATEGORY_OPENABLE)
            }, pickPdf)
        }
        findViewById<Button>(R.id.prev).setOnClickListener { showPage(pageIndex - 1) }
        findViewById<Button>(R.id.next).setOnClickListener { showPage(pageIndex + 1) }
        findViewById<Button>(R.id.pen).setOnClickListener { annotations.tool = Tool.PEN }
        findViewById<Button>(R.id.highlight).setOnClickListener { annotations.tool = Tool.HIGHLIGHTER }
        findViewById<Button>(R.id.erase).setOnClickListener { annotations.tool = Tool.ERASER }
        findViewById<Button>(R.id.undo).setOnClickListener { annotations.undo() }
        findViewById<Button>(R.id.clear).setOnClickListener { annotations.clearAll() }
        findViewById<Button>(R.id.position).setOnClickListener { showPositionMenu(it) }
        setToolbarPosition("RIGHT")
    }

    private fun showPositionMenu(anchor: View) {
        PopupMenu(this, anchor).apply {
            menu.add("Left")
            menu.add("Right")
            menu.add("Top")
            menu.add("Bottom")
            setOnMenuItemClickListener {
                setToolbarPosition(it.title.toString().uppercase())
                true
            }
        }.show()
    }

    private fun setToolbarPosition(position: String) {
        toolbar.orientation = if (position == "LEFT" || position == "RIGHT") LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
        toolbarParams.width = if (position == "LEFT" || position == "RIGHT") dp(64) else -1
        toolbarParams.height = if (position == "TOP" || position == "BOTTOM") dp(58) else -2
        toolbarParams.gravity = when (position) {
            "LEFT" -> Gravity.START or Gravity.CENTER_VERTICAL
            "TOP" -> Gravity.TOP or Gravity.CENTER_HORIZONTAL
            "BOTTOM" -> Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            else -> Gravity.END or Gravity.CENTER_VERTICAL
        }
        toolbar.layoutParams = toolbarParams
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onActivityResult(req: Int, result: Int, data: Intent?) {
        super.onActivityResult(req, result, data)
        if (req == pickPdf && result == RESULT_OK && data?.data != null) openPdf(data.data!!)
    }

    private fun openPdf(uri: Uri) {
        pfd?.close()
        pfd = contentResolver.openFileDescriptor(uri, "r")
        renderer = PdfRenderer(pfd!!)
        pageIndex = 0
        annotations.clearAll()
        showPage(0)
    }

    private fun showPage(i: Int) {
        val r = renderer ?: return
        if (i !in 0 until r.pageCount) return
        pageIndex = i
        val page = r.openPage(i)
        val w = pdfImage.width.coerceAtLeast(resources.displayMetrics.widthPixels - dp(20))
        val scale = w.toFloat() / page.width
        val h = (page.height * scale).toInt().coerceAtLeast(1)
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bmp.eraseColor(Color.WHITE)
        page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        pdfImage.setImageBitmap(bmp)
        pageLabel.text = "${i + 1}/${r.pageCount}"
    }

    override fun onDestroy() {
        renderer?.close(); pfd?.close(); super.onDestroy()
    }
}
