package com.hannesdorfmann.debugoverlay;

import android.content.Context;

/**
 * This implementation does nothing and should be used in production
 *
 * @author Hannes Dorfmann
 */
public class DebugOverlay {
  private static DebugOverlay INSTANCE;

  private DebugOverlay() {}

  public static void init(Context context, int style) {
    if (INSTANCE != null) {
      throw new IllegalStateException("DebugOverlay is already initialized.");
    }
    INSTANCE = new DebugOverlay();
  }

  public static void init(Context context) {
    init(context, 0);
  }

  public static void log(String msg) {}

  public void log(String fortmatedMsg, Object... paramters) {}
}
