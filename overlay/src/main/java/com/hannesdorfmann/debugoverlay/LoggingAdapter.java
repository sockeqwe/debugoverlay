package com.hannesdorfmann.debugoverlay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.StyleRes;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Hannes Dorfmann
 */
public class LoggingAdapter extends SimpleAdapter<String> {
  private final int defStyleRes;

  public LoggingAdapter(Context context, @StyleRes int defStyleRes) {
    super(context);
    this.defStyleRes = defStyleRes;
  }

  @Override public View newView(int type, ViewGroup parent) {
    TextView textView = (TextView) inflater.inflate(R.layout.item_logmsg, parent, false);
    TypedArray typedArray = context.obtainStyledAttributes(null, R.styleable.DebugOverlayView, 0, defStyleRes);
    textView.setTextColor(typedArray.getColor(R.styleable.DebugOverlayView_do_textColor, Color.WHITE));

    int textSize = typedArray.getDimensionPixelSize(R.styleable.DebugOverlayView_do_textSize, 0);
  if (textSize > 0) {
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
  }
    typedArray.recycle();
    return textView;
  }

  @Override public void bindView(int position, int type, View view) {
    TextView tv = (TextView) view;
    tv.setText(items.get(position));
  }
}
