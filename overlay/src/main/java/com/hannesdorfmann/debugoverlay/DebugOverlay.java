package com.hannesdorfmann.debugoverlay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Hannes Dorfmann
 */
public class DebugOverlay {

  private static DebugOverlay INSTANCE;
  private MessageDispatcher messageDispatcher;
  @StyleRes
  private static int style = R.style.DefaultDebugOverlay;

  private DebugOverlay(Context context) {
    Intent intent = new Intent(context, DebugOverlayService.class);
    ServiceConnection serviceConnection = new ServiceConnection() {
      @Override public void onServiceConnected(ComponentName name, IBinder binder) {
        DebugOverlayService service =
            ((DebugOverlayService.DebugOverlayServiceBinder) binder).getService();
        messageDispatcher.setService(service);
      }

      @Override public void onServiceDisconnected(ComponentName name) {
        INSTANCE = null;
      }
    };

    messageDispatcher = new MessageDispatcher();
    boolean bound = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    if (!bound) {
      throw new RuntimeException(
          "Could not bind the Service " + DebugOverlayService.class.getSimpleName()
              + " -  Is Service declared in Android manifest and is Permission SYSTEM_ALERT_WINDOW granted?");
    }
  }

  public static void init(Context context, @StyleRes int style) {
    if (INSTANCE != null) {
      throw new IllegalStateException("DebugOverlay is already initialized.");
    }
    DebugOverlay.style = style;
    INSTANCE = new DebugOverlay(context.getApplicationContext());
  }

  public static void init(Context context) {
    init(context, R.style.DefaultDebugOverlay);
  }

  public static void log(String msg) {
    if (INSTANCE == null) {
      throw new IllegalStateException("DebugOverlay must to be initialized.");
    }
    INSTANCE.messageDispatcher.enqueueMessage(msg);
  }

  @StyleRes
  static int getStyle() {
    return style;
  }

  public void log(String fortmatedMsg, Object... paramters) {
    String msg = String.format(fortmatedMsg, paramters);
    log(msg);
  }

  /**
   * Dispatch Messages to the {@link DebugOverlayService} or store them in a queue if the service
   * is
   * not bound yet. Dispatch the queued messages once we are bound.
   */
  private static class MessageDispatcher {

    private DebugOverlayService service;
    private Queue<String> messageQueue = new LinkedList<>();

    /**
     * DO NOT INVOKE THIS DIRECTLY. Should only be invoked from internally to establish a
     * connection
     * to the service
     */
    void setService(@NonNull DebugOverlayService service) {
      if (service == null) {
        throw new NullPointerException(
            DebugOverlayService.class.getSimpleName() + " is null! That's not allowed");
      }
      this.service = service;
      if (!messageQueue.isEmpty()) {
        for (String msg : messageQueue) {
          dispatch(msg);
        }
      }
    }

    /**
     * Enqueue a message. Will be dispatched directly to the {@link DebugOverlayService} if service
     * is already bound or enqueued into a message queue and be dispatched once the {@link
     * DebugOverlayService} is connected
     *
     * @param msg The message to dispatch
     */
    public void enqueueMessage(@NonNull String msg) {
      if (service != null) {
        dispatch(msg);
      } else {
        messageQueue.add(msg);
      }
    }

    /**
     * Dispatch a message directly
     *
     * @param message The message to dispatch
     */
    private void dispatch(String message) {
      if (service == null) {
        throw new NullPointerException(DebugOverlayService.class.getSimpleName()
            + " is null, but this should never be the case");
      }

      service.logMsg(message);
    }
  }
}
