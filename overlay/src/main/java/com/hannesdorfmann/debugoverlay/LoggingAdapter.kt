package com.hannesdorfmann.debugoverlay

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 *
 *
 * @author Hannes Dorfmann
 */
class LoggingAdapter(context: Context) : SimpleAdapter<String>(context: Context) {

    override fun newView(type: Int, parent: ViewGroup): View {
        return inflater.inflate(R.layout.item_logmsg, parent, false)
    }

    override fun bindView(position: Int, type: Int, view: View) {
        val tv = view as TextView
        tv.text = items?.get(position)
    }
}