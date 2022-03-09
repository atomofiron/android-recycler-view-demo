package app.atomofiron.recyclerview.ultimate.holder

import android.content.res.ColorStateList
import android.view.View
import app.atomofiron.recyclerview.databinding.ItemPictureBinding
import app.atomofiron.recyclerview.ultimate.data.PictureItem
import app.atomofiron.recyclerview.utils.DataItem
import app.atomofiron.recyclerview.utils.GenericViewHolder

class PictureViewHolder(itemView: View) : GenericViewHolder(itemView) {

    private val viewBinding = ItemPictureBinding.bind(itemView)

    init {
        itemView.setOnClickListener {
        }
    }

    override fun bind(data: DataItem) {
        data as PictureItem
        viewBinding.itemPicture.backgroundTintList = ColorStateList.valueOf(data.picture)
        viewBinding.itemString.text = data.index.toString()
    }
}