package app.atomofiron.recyclerview.ultimate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.atomofiron.recyclerview.ultimate.api.UltimateItemListener
import app.atomofiron.recyclerview.ultimate.data.CardItem
import app.atomofiron.recyclerview.ultimate.data.PictureItem
import app.atomofiron.recyclerview.ultimate.data.StringItem
import app.atomofiron.recyclerview.ultimate.delegate.DataItemsDelegate
import app.atomofiron.recyclerview.ultimate.delegate.DataItemsDelegateImpl
import app.atomofiron.recyclerview.ultimate.utils.UltimateViewHolderFactory
import app.atomofiron.recyclerview.utils.GenericViewHolder
import java.lang.Exception

class UltimateAdapter : RecyclerView.Adapter<GenericViewHolder>(), DataItemsDelegate by DataItemsDelegateImpl() {

    private var itemListener: UltimateItemListener? = null

    init {
        adapter = this // небольшой костыль для DataItemsDelegateImpl
        setHasStableIds(true)
    }

    fun setListener(listener: UltimateItemListener?) {
        itemListener = listener
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].id

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is CardItem -> UltimateViewHolderFactory.CARD.ordinal
        is PictureItem -> UltimateViewHolderFactory.PICTURE.ordinal
        is StringItem -> UltimateViewHolderFactory.STRING.ordinal
        else -> throw Exception()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val holder = UltimateViewHolderFactory.values()[viewType].createHolder(parent)
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            if (position != -1) {
                itemListener?.onItemClick(position, items[position])
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(
        holder: GenericViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }
}