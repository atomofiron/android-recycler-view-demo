package app.atomofiron.recyclerview.medium

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import app.atomofiron.recyclerview.R
import app.atomofiron.recyclerview.utils.DataItem

class MediumListAdapter : ListAdapter<DataItem, MediumViewHolder>(MediumItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_string, parent, false)
        itemView.setOnClickListener { }
        return MediumViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MediumViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}