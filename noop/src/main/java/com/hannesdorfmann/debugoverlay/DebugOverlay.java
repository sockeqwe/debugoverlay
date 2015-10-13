package com.hannesdorfmann.debugoverlay;

import android.content.Context;

/**
 * This implementation does nothing and should be used in production
 *
 * @author Hannes Dorfmann
 */
public class DebugOverlay {

  private static final DebugOverlay INSTANCE = new DebugOverlay();

  private DebugOverlay() {
  }

  public static DebugOverlay with(Context context) {
    return INSTANCE;
  }

  public DebugOverlay log(String msg) {
    return this;
  }

  public DebugOverlay log(String fortmatedMsg, Object... paramters) {
    return this;
  }
}
