package com.hannesdorfmann.debugoverlay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Simple UI that displays a list of log messages
 *
 * @author Hannes Dorfmann
 */
class DebugOverlayView extends FrameLayout {

  private ImageView closeButton;
  private WindowManager windowManager;
  private LoggingAdapter adapter;
  private ListView listView;

  public DebugOverlayView(Context context) {
    super(context);

    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Point windowDimen = new Point();
    windowManager.getDefaultDisplay().getSize(windowDimen);

    int desiredLayoutHeight = dpToPx(context, 100);
    int layoutHeight = desiredLayoutHeight < windowDimen.y ? desiredLayoutHeight : windowDimen.y;

    // Setup the GUI

    // Close Button
    int buttonHeight = dpToPx(context, 40);
    closeButton = new ImageView(context);
    closeButton.setImageResource(R.drawable.ic_close_circle);
    closeButton.setLayoutParams(
        new FrameLayout.LayoutParams(buttonHeight, buttonHeight, Gravity.TOP | Gravity.RIGHT));

    // Logging Console
    adapter = new LoggingAdapter(context);
    listView = new ListView(context);
    listView.setBackgroundColor(Color.parseColor("#64000000"));
    listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    listView.setStackFromBottom(true);
    listView.setAdapter(adapter);
    FrameLayout.LayoutParams listViewLayoutParams =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
    listViewLayoutParams.topMargin = buttonHeight / 2;
    listView.setLayoutParams(listViewLayoutParams);

    // Add views
    addView(listView);
    addView(closeButton);

    // Set View parameters
    WindowManager.LayoutParams windowParams =
        new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight,
            WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    windowParams.gravity = Gravity.TOP | Gravity.LEFT;
    windowParams.x = 0;
    windowParams.y = windowDimen.y - layoutHeight;

    // Attach and display View
    windowManager.addView(this, windowParams);
  }

  private int dpToPx(Context context, int dp) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return dp * (metrics.densityDpi / 160);
  }

  void addMessage(String msg) {
    adapter.getItems().add(msg);
    adapter.notifyDataSetChanged();
  }

  void hideView() {
    windowManager.removeView(this);
  }

  View getCloseButton() {
    return closeButton;
  }
}
