package com.hannesdorfmann.debugoverlay;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author Hannes Dorfmann
 */
public class DebugOverlayService extends Service {

  public static class DebugOverlayServiceBinder extends Binder {
    private DebugOverlayService service;

    public DebugOverlayServiceBinder(DebugOverlayService service) {
      this.service = service;
    }

    public DebugOverlayService getService() {
      return service;
    }
  }

  private DebugOverlayServiceBinder binder = new DebugOverlayServiceBinder(this);
  private DebugOverlayView view;

  @Nullable @Override public IBinder onBind(Intent intent) {
    return binder;
  }

  public void logMsg(String msg) {
    if (view == null) {
      view = new DebugOverlayView(getApplicationContext());
      view.getCloseButton().setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          destroyView();
          stopSelf();
        }
      });
    }

    view.addMessage(msg);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    destroyView();
  }

  private void destroyView() {
    if (view != null) {
      view.hideView();
      view = null;
    }
  }
}
