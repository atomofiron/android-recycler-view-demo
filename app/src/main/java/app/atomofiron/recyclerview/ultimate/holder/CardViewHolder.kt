package app.atomofiron.recyclerview.ultimate.holder

import android.icu.text.SimpleDateFormat
import android.view.View
import app.atomofiron.recyclerview.databinding.ItemCardBinding
import app.atomofiron.recyclerview.ultimate.data.CardItem
import app.atomofiron.recyclerview.utils.DataItem
import app.atomofiron.recyclerview.utils.GenericViewHolder
import java.util.*

class CardViewHolder(itemView: View) : GenericViewHolder(itemView) {

    private val viewBinding = ItemCardBinding.bind(itemView)

    init {
        itemView.setOnClickListener {
        }
    }

    override fun bind(data: DataItem) {
        data as CardItem
        viewBinding.itemPicture.setBackgroundColor(data.picture)
        viewBinding.itemString.text = data.string
        val date = SimpleDateFormat.getDateInstance().format(Date(data.timestamp))
        viewBinding.itemDate.text = date
    }
}