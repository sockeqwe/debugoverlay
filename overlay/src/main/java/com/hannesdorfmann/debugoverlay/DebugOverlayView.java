package com.hannesdorfmann.debugoverlay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
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
  private ImageView upButton;
  private ImageView downButton;
  private WindowManager windowManager;
  private LoggingAdapter adapter;
  private ListView listView;

  public DebugOverlayView(Context context) {
    super(context);

    windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Point windowDimen = new Point();
    windowManager.getDefaultDisplay().getSize(windowDimen);

    // Use 1/2 of screen height for overlay
    int desiredLayoutHeight = windowDimen.y / 2;
    int layoutHeight = Math.min(desiredLayoutHeight, windowDimen.y);

    // Setup the GUI

    // Buttons
    int buttonHeight = dpToPx(context, 40);
    closeButton = new ImageView(context);
    closeButton.setImageResource(R.drawable.ic_close_circle);
    closeButton.setLayoutParams(new FrameLayout.LayoutParams(buttonHeight, buttonHeight, Gravity.TOP | Gravity.END));
    upButton = new ImageView(context);
    upButton.setImageResource(R.drawable.ic_up_circle);
    upButton.setLayoutParams(new FrameLayout.LayoutParams(buttonHeight, buttonHeight, Gravity.CENTER | Gravity.END));
    downButton = new ImageView(context);
    downButton.setImageResource(R.drawable.ic_down_circle);
    downButton.setLayoutParams(new FrameLayout.LayoutParams(buttonHeight, buttonHeight, Gravity.BOTTOM | Gravity.END));

    // Logging Console
    adapter = new LoggingAdapter(context);
    listView = new ListView(context);
    listView.setBackgroundColor(Color.parseColor("#64000000"));
    listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    listView.setStackFromBottom(true);
    listView.setAdapter(adapter);
    FrameLayout.LayoutParams listViewLayoutParams =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    listViewLayoutParams.topMargin = buttonHeight / 2;
    listView.setLayoutParams(listViewLayoutParams);

    // Add views
    addView(listView);
    addView(closeButton);
    addView(upButton);
    addView(downButton);

    upButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          int firstVisiblePosition = listView.getFirstVisiblePosition();
          int lastVisiblePosition = listView.getLastVisiblePosition();
            listView.smoothScrollToPosition(firstVisiblePosition - (lastVisiblePosition - firstVisiblePosition));
        }
      }
    );

    downButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          int lastVisiblePosition = listView.getLastVisiblePosition();
          listView.smoothScrollToPosition(lastVisiblePosition+1);
        }
      }
    );

    // Set View parameters
    WindowManager.LayoutParams windowParams;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      windowParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight,
          WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
    } else {
      windowParams =
          new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight, WindowManager.LayoutParams.TYPE_PHONE,
              WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
    }

    windowParams.gravity = Gravity.TOP | Gravity.START;
    windowParams.x = 0;
    windowParams.y = 100;

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
