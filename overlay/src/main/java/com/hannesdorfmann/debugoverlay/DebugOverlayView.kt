package com.hannesdorfmann.debugoverlay

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Point
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*

/**
 * Simple UI that displays a list of log messages
 * @author Hannes Dorfmann
 */
class DebugOverlayView(context: Context) : FrameLayout(context : Context) {

    val closeButton: ImageView
    private val windowManager: WindowManager
    private val adapter: LoggingAdapter
    private val listView: ListView

    init {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowDimen = Point()
        windowManager.defaultDisplay.getSize(windowDimen)

        val desiredLayoutHeight = dpToPx(context, 100)
        val layoutHeight = if (desiredLayoutHeight < windowDimen.y) desiredLayoutHeight else windowDimen.y


        // Setup the GUI

        // Close Button
        val buttonHeight = dpToPx(context, 40)
        closeButton = ImageView(context)
        closeButton.setImageResource(R.drawable.ic_close_circle)
        closeButton.layoutParams = FrameLayout.LayoutParams(buttonHeight, buttonHeight, Gravity.TOP or Gravity.RIGHT)


        // Logging Console
        adapter = LoggingAdapter(context)
        listView = ListView(context)
        listView.setBackgroundColor(Color.parseColor("#64000000"))
        listView.transcriptMode = AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL
        listView.setStackFromBottom(true)
        listView.adapter = adapter
        val listViewLayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        listViewLayoutParams.topMargin = buttonHeight / 2
        listView.layoutParams = listViewLayoutParams


        // Add views
        addView(listView)
        addView(closeButton)


        // Set View parameters
        val windowParams = WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                layoutHeight,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        windowParams.gravity = Gravity.TOP or Gravity.LEFT;
        windowParams.x = 0;
        windowParams.y = windowDimen.y - layoutHeight

        // Attach and display View
        windowManager.addView(this, windowParams)

    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val metrics = context.resources.displayMetrics;
        val px = dp * (metrics.densityDpi / 160);
        return px;
    }

    internal fun addMessage(msg: String) {
        adapter.items.add(msg)
        adapter.notifyDataSetChanged()
    }

    internal fun hideView() {
        windowManager.removeView(this)
    }
}
