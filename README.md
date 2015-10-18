# DebugOverlay
A `logcat` alike overlay to display log messages in your app as independent overlay.

Typically in android you would log some app internals for debugging puropose by using `Log.d()`. However, sometimes you may need to display that logging messages right on the screen to make them visible for non developers that have to verify certain app internals, like tracking.

![DebugOverlay](http://hannesdorfmann.com/images/debugoverlay.png)

This tiny library adds an overlay as independet `Window` on top of your android application. You can close this `DebugOverlay`-Window by clicking on the close button. The window will then automatically pop up again when the next message to log is detected.

## How to use it
```java
DebugOverlay.with(context).log("My logging message");
```

## Dependencies
Obviously you wont deliver this `DebugOverlay` with you production code in the play store .apk file. Hence this library provides two maven artifacts to include.
```groovy
debugCompile('com.hannesdorfmann:debugoverlay:0.2.0') // Starts the service and displays the overlay
releaseCompile('com.hannesdorfmann:debugoverlay-noop:0.2.0') // Does nothing
```

The idea is to use gradle build types to add the `DebugOverlay` only to debug builds.
`debugoverlay` is the real implementation and displays the window while `debugoverlay-noop` is a stub and simply does nothing!

## Permissions
Please note that `com.hannesdorfmann:debugoverlay:0.2.0` will add `android.permission.SYSTEM_ALERT_WINDOW` to your apk. Hence you should avoid to use that dependency for your release `.apk`

## Features
The api is very simple. You can simply log string messages. You can open new issues here on github to make a new feature request. If it is not to complicated and will not add a lot of new methods to the public API (dex count 65k limit for debugoverlay-noop that might be part of your release build) then it's very likely that we can add this feature to this library.
