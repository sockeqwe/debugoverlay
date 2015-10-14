package com.hannesdorfmann.debugoverlay;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Hannes Dorfmann
 */
public class LoggingAdapter extends SimpleAdapter<String> {

  public LoggingAdapter(Context context) {
    super(context);
  }

  @Override public View newView(int type, ViewGroup parent) {
    return inflater.inflate(R.layout.item_logmsg, parent, false);
  }

  @Override public void bindView(int position, int type, View view) {
    TextView tv = (TextView) view;
    tv.setText(items.get(position));
  }
}
