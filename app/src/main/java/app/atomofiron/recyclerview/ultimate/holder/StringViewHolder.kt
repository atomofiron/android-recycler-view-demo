package app.atomofiron.recyclerview.ultimate.holder

import android.view.View
import app.atomofiron.recyclerview.databinding.ItemStringBinding
import app.atomofiron.recyclerview.ultimate.data.StringItem
import app.atomofiron.recyclerview.utils.DataItem
import app.atomofiron.recyclerview.utils.GenericViewHolder

class StringViewHolder(itemView: View) : GenericViewHolder(itemView) {

    private val viewBinding = ItemStringBinding.bind(itemView)

    init {
        itemView.setOnClickListener {
        }
    }

    override fun bind(data: DataItem) {
        data as StringItem
        viewBinding.itemString.text = data.string
    }
}