package com.hannesdorfmann.debugoverlay

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.*

/**
 * This [BaseAdapter] encapsulate the getView() call into
 * [.newView] and [.bindView]

 * @author Hannes Dorfmann
 */
internal abstract class SimpleAdapter<D>(protected var context: Context) : BaseAdapter() {

    /**
     * The inflater for
     */
    protected var inflater: LayoutInflater
    val items: MutableList<D> = ArrayList<D>()

    init {
        this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return items.size()
    }

    override fun getItem(position: Int): D {
        return items.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val type = getItemViewType(position)
        if (convertView == null) {
            convertView = newView(type, parent)
        }
        bindView(position, type, convertView)
        return convertView
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    /** Create a new instance of a view for the specified `type`.  */
    abstract fun newView(type: Int, parent: ViewGroup): View

    /** Bind the data for the specified `position` to the `view`.  */
    abstract fun bindView(position: Int, type: Int, view: View)
}