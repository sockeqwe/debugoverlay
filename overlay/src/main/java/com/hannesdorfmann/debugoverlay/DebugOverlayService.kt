package com.hannesdorfmann.debugoverlay

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * @author Hannes Dorfmann
 */
class DebugOverlayService : Service() {

    /**
     * Binder interface
     */
    internal class DebugOverlayServiceBinder(internal val  service: DebugOverlayService) : Binder() {
    }

    private val binder = DebugOverlayServiceBinder(this)
    private var view: DebugOverlayView? = null

    override fun onBind(intent: Intent?): IBinder? {
        return binder;
    }

    public fun logMsg(msg: String) {
        if (view == null) {
            view = DebugOverlayView(applicationContext)
            view!!.closeButton.setOnClickListener {
                destroyView()
                stopSelf()
            }
        }
        view?.addMessage(msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyView()
    }

    fun destroyView() {
        view?.hideView()
        view = null
    }
}
