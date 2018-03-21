package com.hannesdorfmann.debugoverlay.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.hannesdorfmann.debugoverlay.DebugOverlay;

public class MainActivity extends AppCompatActivity {

  public static int i = 0;
  private static int APP_PERMISSIONS = 1234;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    DebugOverlay.init(MainActivity.this);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        boolean showDebugOverlay = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(MainActivity.this);
        if (showDebugOverlay) {
          DebugOverlay.log("Message test " + i++);
        } else {
          requestOverlayPermission();
        }
      }
    });
  }

  private void requestOverlayPermission() {
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
      return;
    }

    Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
    myIntent.setData(Uri.parse("package:" + getPackageName()));
    startActivityForResult(myIntent, APP_PERMISSIONS);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && requestCode == APP_PERMISSIONS) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Settings.canDrawOverlays(this)) {
          DebugOverlay.log("Message test " + i++);
        }
      } else {
        DebugOverlay.log("Message test " + i++);
      }
    }

    super.onActivityResult(requestCode, resultCode, data);
  }
}
