package app.atomofiron.recyclerview.list_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import app.atomofiron.recyclerview.R

class ListViewAdapter : BaseAdapter() {

    private val items: MutableList<String> = IntArray(25) { it }.map { "Item $it" }.toMutableList()

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): String = items[position]

    override fun getItemId(position: Int): Long = getItem(position).hashCode().toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = when (convertView) {
            null -> LayoutInflater.from(parent.context).inflate(R.layout.item_string, parent, false)
            else -> convertView
        }
        val tv = itemView.findViewById<TextView>(R.id.item_string)
        tv.text = getItem(position)
        return itemView
    }
}