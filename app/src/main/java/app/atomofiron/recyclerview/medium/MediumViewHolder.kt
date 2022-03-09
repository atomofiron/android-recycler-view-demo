package app.atomofiron.recyclerview.medium

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.atomofiron.recyclerview.R
import app.atomofiron.recyclerview.ultimate.data.StringItem
import app.atomofiron.recyclerview.utils.DataItem

class MediumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.item_string)

    fun bind(item: DataItem) {
        item as StringItem
        title.text = item.string
    }
}